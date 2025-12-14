package hu.webarticum.miniconnect.lang;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.Period;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalUnit;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class DateTimeDelta implements Comparable<DateTimeDelta>, TemporalAmount, Serializable {

    private static final long serialVersionUID = 1L;


    private enum IntervalUnit {

        NANOSECOND(ChronoUnit.NANOS, "nanos"),
        MICROSECOND(ChronoUnit.MICROS, "micros"),
        MILLISECOND(ChronoUnit.MILLIS, "millis"),
        SECOND(ChronoUnit.SECONDS),
        MINUTE(ChronoUnit.MINUTES),
        HOUR(ChronoUnit.HOURS),
        DAY(ChronoUnit.DAYS),
        WEEK(ChronoUnit.WEEKS),
        MONTH(ChronoUnit.MONTHS),
        YEAR(ChronoUnit.YEARS),
        DECADE(ChronoUnit.DECADES),
        CENTURY(ChronoUnit.CENTURIES, "centuries"),
        MILLENNIUM(ChronoUnit.MILLENNIA, "millennia"),
        ERA(ChronoUnit.ERAS),

        ;

        final TemporalUnit temporalUnit;

        final String alternativePluralName;

        private IntervalUnit(TemporalUnit temporalUnit) {
            this(temporalUnit, null);
        }

        private IntervalUnit(TemporalUnit temporalUnit, String alternativePluralName) {
            this.temporalUnit = temporalUnit;
            this.alternativePluralName = alternativePluralName;
        }

        boolean matches(String name) {
            return
                    name().equalsIgnoreCase(name) ||
                    (name() + "s").equalsIgnoreCase(name) ||
                    (alternativePluralName != null && alternativePluralName.equalsIgnoreCase(name));
        }

        String matchRegex() {
            String result = name().toLowerCase() + "s?";
            return alternativePluralName != null ? result + "|" + alternativePluralName : result;
        }

        static IntervalUnit ofName(String name) {
            for (IntervalUnit unit : IntervalUnit.values()) {
                if (unit.matches(name)) {
                    return unit;
                }
            }
            throw new IllegalArgumentException("No matching unit for " + name);
        }

    }


    private static final int SECONDS_PER_DAY = 86400;

    private static final int ESTIMATED_DAYS_PER_MONTH = 30;

    private static final int MONTHS_PER_YEAR = 12;

    private static final List<TemporalUnit> SUPPORTED_UNITS = Collections.unmodifiableList(Arrays.<TemporalUnit>asList(
            ChronoUnit.YEARS, ChronoUnit.MONTHS, ChronoUnit.DAYS, ChronoUnit.SECONDS, ChronoUnit.NANOS));

    private static final Pattern SECONDS_PATTERN = Pattern.compile(
            " *(?:@ *)?" +
            "(?:(?:(?<neg>\\-)|\\+) *)?" +
            "(?:(?<s0>\\d+)\\.?|" +
            "(?<s>\\d+)?\\.(?<u>\\d+))" +
            " *");

    private static final Pattern SQL_INTERVAL_PATTERN = Pattern.compile(
            " *(?:@ *)?" +
            "((?<M00>\\d+) +(?<D00>\\d+)|" +
            "(?<Y0>\\d+)\\-(?<M0>\\d+)(?: +(?<D0>\\d+))?|" +
            "(?:(?:(?:(?<Y>\\d+)\\-)?(?<M>\\d+) +)?(?<D>\\d+) +)?(?<h>\\d+):(?<m>\\d+)(?::(?:\\.(?<u0>\\d+)|(?<s>\\d+)(?:\\.(?<u>\\d+)?)?))?)" +
            "(?<ago> +ago)?" +
            " *");

    private static final Pattern PG_VERBOSE_ITEM_PATTERN;
    static {
        StringBuilder pgVerboseItemRegexBuilder = new StringBuilder("\\G *(?:(?:(?:(?<neg>\\-)|\\+) *)?\\b(?<amount>\\d+) *(?<unit>");
        boolean first = true;
        for (IntervalUnit unit : IntervalUnit.values()) {
            if (first) {
                first = false;
            } else {
                pgVerboseItemRegexBuilder.append('|');
            }
            pgVerboseItemRegexBuilder.append(unit.matchRegex());
        }
        pgVerboseItemRegexBuilder.append(")|\\b(?<ago>ago\\b)|(?<eof>$)|(?<at>@))");
        PG_VERBOSE_ITEM_PATTERN = Pattern.compile(pgVerboseItemRegexBuilder.toString(), Pattern.CASE_INSENSITIVE);
    }


    public static final DateTimeDelta ZERO = new DateTimeDelta(Period.ZERO, Duration.ZERO);

    public static final DateTimeDelta MAX_VALUE = new DateTimeDelta(
            Period.of(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE), Duration.ofSeconds(Long.MAX_VALUE, 999_999_999));

    public static final DateTimeDelta MIN_VALUE = new DateTimeDelta(
            Period.of(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE), Duration.ofSeconds(Long.MIN_VALUE, 0));


    private final Period period;

    private final Duration duration;


    private DateTimeDelta(Period period, Duration duration) {
        this.period = Objects.requireNonNull(period);
        this.duration = Objects.requireNonNull(duration);
    }

    public static DateTimeDelta of(Period period) {
        return of(period, Duration.ZERO);
    }

    public static DateTimeDelta of(Duration duration) {
        return of(Period.ZERO, duration);
    }

    public static DateTimeDelta of(Period period, Duration duration) {
        return new DateTimeDelta(period, duration);
    }

    public static DateTimeDelta of(long amount, TemporalUnit unit) {
        return ZERO.plus(amount, unit);
    }

    public static DateTimeDelta of(int years, int months, int days, long seconds) {
        return new DateTimeDelta(Period.of(years, months, days), Duration.ofSeconds(seconds));
    }

    public static DateTimeDelta of(int years, int months, int days, long seconds, long nanos) {
        return new DateTimeDelta(Period.of(years, months, days), Duration.ofSeconds(seconds, nanos));
    }

    public static DateTimeDelta from(TemporalAmount amount) {
        return ZERO.plus(amount);
    }

    public static DateTimeDelta between(Temporal startInclusive, Temporal endExclusive) {
        LocalDate fallbackDate = getPreferredLocalDate(startInclusive, endExclusive);
        ZoneOffset fallbackOffset = getPreferredZoneOffset(startInclusive, endExclusive);
        return between(toOffsetDateTime(startInclusive, fallbackDate, fallbackOffset), toOffsetDateTime(endExclusive, fallbackDate, fallbackOffset));
    }

    private static DateTimeDelta between(OffsetDateTime startInclusive, OffsetDateTime endExclusive) {
        LocalDate date1 = startInclusive.toLocalDate();
        LocalDate date2 = endExclusive.toLocalDate();
        OffsetTime time1 = startInclusive.toOffsetTime();
        OffsetTime time2 = endExclusive.toOffsetTime();
        Duration duration = Duration.between(time1, time2);
        if (date1.compareTo(date2) < 0) {
            while (duration.isNegative()) {
                date2 = date2.minusDays(1);
                duration = duration.plusDays(1);
            }
        } else if (date1.compareTo(date2) > 0) {
            while (!duration.isNegative() && !duration.isZero()) {
                date2 = date2.plusDays(1);
                duration = duration.minusDays(1);
            }
        }
        long durationDays = duration.toDays();
        if (durationDays != 0) {
            date2 = date2.plusDays(durationDays);
            duration = duration.minusDays(durationDays);
        }
        Period period = Period.between(date1, date2);
        return DateTimeDelta.of(period, duration);
    }

    private static LocalDate getPreferredLocalDate(Temporal temporal1, Temporal temporal2) {
        LocalDate date1 = getLocalDate(temporal1);
        LocalDate date2 = getLocalDate(temporal2);
        if (date1 != null) {
            return date1;
        } else if (date2 != null) {
            return date2;
        } else {
            return LocalDate.MIN;
        }
    }

    private static LocalDate getLocalDate(Temporal temporal) {
        if (temporal instanceof LocalDate) {
            return (LocalDate) temporal;
        } else if (temporal instanceof LocalDateTime) {
            return ((LocalDateTime) temporal).toLocalDate();
        } else if (temporal instanceof OffsetDateTime) {
            return ((OffsetDateTime) temporal).toLocalDate();
        } else if (temporal instanceof ZonedDateTime) {
            return ((ZonedDateTime) temporal).toLocalDate();
        } else if (temporal instanceof Instant) {
            return ((Instant) temporal).atOffset(ZoneOffset.UTC).toLocalDate();
        } else {
            return null;
        }
    }

    private static ZoneOffset getPreferredZoneOffset(Temporal temporal1, Temporal temporal2) {
        ZoneOffset offset1 = getZoneOffset(temporal1);
        ZoneOffset offset2 = getZoneOffset(temporal2);
        if (offset1 != null) {
            return offset1;
        } else if (offset2 != null) {
            return offset2;
        } else {
            return ZoneOffset.UTC;
        }
    }

    private static ZoneOffset getZoneOffset(Temporal temporal) {
        if (temporal instanceof OffsetDateTime) {
            return ((OffsetDateTime) temporal).getOffset();
        } else if (temporal instanceof ZonedDateTime) {
            return ((ZonedDateTime) temporal).getOffset();
        } else if (temporal instanceof OffsetTime) {
            return ((OffsetTime) temporal).getOffset();
        } else if (temporal instanceof Instant) {
            return ZoneOffset.UTC;
        } else {
            return null;
        }
    }

    private static OffsetDateTime toOffsetDateTime(Temporal temporal, LocalDate fallbackDate, ZoneOffset fallbackOffset) {
        if (temporal instanceof OffsetDateTime) {
            return (OffsetDateTime) temporal;
        } else if (temporal instanceof Instant) {
            return ((Instant) temporal).atOffset(fallbackOffset);
        } else if (temporal instanceof LocalDateTime) {
            return ((LocalDateTime) temporal).atOffset(fallbackOffset);
        } else if (temporal instanceof ZonedDateTime) {
            return ((ZonedDateTime) temporal).toOffsetDateTime();
        } else if (temporal instanceof LocalDate) {
            return ((LocalDate) temporal).atStartOfDay().atOffset(ZoneOffset.UTC);
        } else if (temporal instanceof OffsetTime) {
            return ((OffsetTime) temporal).atDate(fallbackDate);
        } else if (temporal instanceof LocalTime) {
            return ((LocalTime) temporal).atOffset(fallbackOffset).atDate(fallbackDate);
        } else {
            throw new UnsupportedTemporalTypeException("Unexpected type as boundary: " + temporal.getClass());
        }
    }

    public static DateTimeDelta parse(String deltaString) {
        Objects.requireNonNull(deltaString);
        if (deltaString.isEmpty()) {
            throw new DateTimeParseException("Empty delta string", deltaString, 0);
        }

        if (
                deltaString.charAt(0) == 'P' ||
                deltaString.startsWith("-P") ||
                deltaString.charAt(0) == 'p' ||
                deltaString.startsWith("-p")) {
            return parseIso8601(deltaString);
        }

        Matcher secondsMatcher = SECONDS_PATTERN.matcher(deltaString);
        if (secondsMatcher.matches()) {
            return createFromSecondsMatcher(secondsMatcher);
        }

        Matcher sqlIntervalMatcher = SQL_INTERVAL_PATTERN.matcher(deltaString);
        if (sqlIntervalMatcher.matches()) {
            return createFromSqlIntervalMatcher(sqlIntervalMatcher);
        }

        return parsePgVerbose(deltaString);
    }

    private static DateTimeDelta parseIso8601(String deltaString) {
        int tPos = deltaString.indexOf('T');
        if (tPos == -1) {
            tPos = deltaString.indexOf('t');
        }
        boolean isNegated = deltaString.charAt(0) == '-';
        int periodPrefixLength = isNegated ? 2 : 1;
        String periodString = tPos >= 0 ? deltaString.substring(0, tPos) : deltaString;
        Period period = periodString.length() > periodPrefixLength ? Period.parse(periodString) : Period.ZERO;
        Duration duration = Duration.ZERO;
        if (tPos >= 0) {
            String prefix = isNegated ? "-P" : "P";
            String durationString = prefix + deltaString.substring(tPos);
            duration = Duration.parse(durationString);
        }
        return new DateTimeDelta(period, duration);
    }

    private static DateTimeDelta createFromSecondsMatcher(Matcher matcher) {
        int nanos = parseFractionAsNanos(extractFirst(matcher, "u"));
        long seconds = extractLong(matcher, "s", "s0");
        boolean negate = matcher.group("neg") != null;
        DateTimeDelta result = DateTimeDelta.of(Duration.ofSeconds(seconds, nanos));
        return negate ? result.negated() : result;
    }

    private static DateTimeDelta createFromSqlIntervalMatcher(Matcher matcher) {
        int nanos = parseFractionAsNanos(extractFirst(matcher, "u", "u0"));
        int seconds = extractInt(matcher, "s");
        int minutes = extractInt(matcher, "m");
        int hours = extractInt(matcher, "h");
        int days = extractInt(matcher, "D", "D0", "D00");
        int months = extractInt(matcher, "M", "M0", "M00");
        int years = extractInt(matcher, "Y", "Y0");
        boolean negate = matcher.group("ago") != null;
        int fullSeconds = (3600 * hours) + (60* minutes) + seconds;
        DateTimeDelta result = of(Period.of(years, months, days), Duration.ofSeconds(fullSeconds, nanos));
        return negate ? result.negated() : result;
    }

    private static int parseFractionAsNanos(String nullableNanosString) {
        if (nullableNanosString == null) {
            return 0;
        }

        int result = Integer.parseInt(nullableNanosString);
        for (int i = nullableNanosString.length(); i < 9; i++) {
            result *= 10;
        }
        return result;
    }

    private static long extractLong(Matcher matcher, String... groupNames) {
        String longStr = extractFirst(matcher, groupNames);
        return (longStr != null) ? Long.parseLong(longStr) : 0;
    }

    private static int extractInt(Matcher matcher, String... groupNames) {
        String intStr = extractFirst(matcher, groupNames);
        return (intStr != null) ? Integer.parseInt(intStr) : 0;
    }

    private static String extractFirst(Matcher matcher, String... groupNames) {
        for (String groupName : groupNames) {
            String matchStr = matcher.group(groupName);
            if (matchStr != null) {
                return matchStr;
            }
        }
        return null;
    }

    private static DateTimeDelta parsePgVerbose(String deltaString) {
        boolean agoGiven = false;
        boolean endReached = false;
        EnumMap<IntervalUnit, Long> valueMap = new EnumMap<>(IntervalUnit.class);
        Matcher itemMatcher = PG_VERBOSE_ITEM_PATTERN.matcher(deltaString);
        while (itemMatcher.find()) {
            String amountStr = itemMatcher.group("amount");
            if (amountStr != null) {
                if (agoGiven) {
                    throw new DateTimeParseException("Unexpected value after ago", deltaString, itemMatcher.start());
                }
                IntervalUnit unit = IntervalUnit.ofName(itemMatcher.group("unit"));
                if (valueMap.containsKey(unit)) {
                    throw new DateTimeParseException("Duplicated value for " + unit.name(), deltaString, itemMatcher.start());
                }
                long amount = Long.parseLong(amountStr);
                boolean negated = itemMatcher.group("neg") != null;
                amount = negated ? -amount : amount;
                valueMap.put(unit, amount);
            } else if (itemMatcher.group("eof") != null) {
                if (valueMap.isEmpty()) {
                    throw new DateTimeParseException("Unexpected end of input", deltaString, itemMatcher.start());
                }
                endReached = true;
            } else if (itemMatcher.group("ago") != null) {
                if (agoGiven || valueMap.isEmpty()) {
                    throw new DateTimeParseException("Unexpected ago", deltaString, itemMatcher.start());
                }
                agoGiven = true;
            } else if (itemMatcher.group("at") != null) {
                if (!valueMap.isEmpty()) {
                    throw new DateTimeParseException("Unexpected at sign", deltaString, itemMatcher.start());
                }
            }
        }

        if (!endReached) {
            throw new DateTimeParseException("Unexpected input", deltaString, 0);
        }

        DateTimeDelta result = DateTimeDelta.ZERO;
        for (Map.Entry<IntervalUnit, Long> entry : valueMap.entrySet()) {
            result = result.plus(entry.getValue(), entry.getKey().temporalUnit);
        }
        if (agoGiven) {
            result = result.negated();
        }
        return result;
    }


    public Period getPeriod() {
        return period;
    }

    public Duration getDuration() {
        return duration;
    }

    @Override
    public List<TemporalUnit> getUnits() {
        return SUPPORTED_UNITS;
    }

    @Override
    public long get(TemporalUnit unit) {
        if (unit == ChronoUnit.YEARS || unit == ChronoUnit.MONTHS || unit == ChronoUnit.DAYS) {
            return period.get(unit);
        } else if (unit == ChronoUnit.SECONDS || unit == ChronoUnit.NANOS) {
            return duration.get(unit);
        } else {
            throw new UnsupportedTemporalTypeException("Unsupported unit: " + unit);
        }
    }

    @Override
    public Temporal addTo(Temporal temporal) {
        if (temporal instanceof Instant) {
            OffsetDateTime dateTime = ((Instant) temporal).atOffset(ZoneOffset.UTC);
            OffsetDateTime changedDateTime = (OffsetDateTime) duration.addTo(period.addTo(dateTime));
            return changedDateTime.toInstant();
        }

        return duration.addTo(period.addTo(temporal));
    }

    @Override
    public Temporal subtractFrom(Temporal temporal) {
        if (temporal instanceof Instant) {
            OffsetDateTime dateTime = ((Instant) temporal).atOffset(ZoneOffset.UTC);
            OffsetDateTime changedDateTime = (OffsetDateTime) duration.subtractFrom(period.subtractFrom(dateTime));
            return changedDateTime.toInstant();
        }

        return duration.subtractFrom(period.subtractFrom(temporal));
    }

    @Override
    public int compareTo(DateTimeDelta other) {
        int cmp = Integer.compare(period.getYears(), other.period.getYears());
        if (cmp != 0) {
            return cmp;
        }

        cmp = Integer.compare(period.getMonths(), other.period.getMonths());
        if (cmp != 0) {
            return cmp;
        }

        cmp = Integer.compare(period.getDays(), other.period.getDays());
        if (cmp != 0) {
            return cmp;
        }

        return duration.compareTo(other.duration);
    }

    @Override
    public int hashCode() {
        return (period.hashCode() * 37) + duration.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (this == obj) {
            return true;
        } else if (!(obj instanceof DateTimeDelta)) {
            return false;
        }

        DateTimeDelta otherDelta = (DateTimeDelta) obj;
        return period.equals(otherDelta.period) && duration.equals(otherDelta.duration);
    }

    @Override
    public String toString() {
        if (period.isZero()) {
            return duration.toString();
        } else if (duration.isZero()) {
            return period.toString();
        } else {
            return period.toString() + duration.toString().substring(1);
        }
    }

    public boolean isZero() {
        return period.isZero() && duration.isZero();
    }

    public boolean isNegative() {
        return period.isNegative() || duration.isNegative();
    }

    public DateTimeDelta negated() {
        return of(period.negated(), duration.negated());
    }

    public DateTimeDelta multipliedBy(int scalar) {
        return of(period.multipliedBy(scalar), duration.multipliedBy(scalar));
    }

    public DateTimeDelta plus(TemporalAmount amountToAdd) {
        if (amountToAdd instanceof DateTimeDelta) {
            DateTimeDelta otherDelta = (DateTimeDelta) amountToAdd;
            return DateTimeDelta.of(period.plus(otherDelta.period), duration.plus(otherDelta.duration));
        } else if (amountToAdd instanceof Duration) {
            Duration otherDuration = (Duration) amountToAdd;
            return DateTimeDelta.of(period, duration.plus(otherDuration));
        } else if (amountToAdd instanceof Period) {
            Period otherPeriod = (Period) amountToAdd;
            return DateTimeDelta.of(period.plus(otherPeriod), duration);
        }

        DateTimeDelta result = DateTimeDelta.ZERO;
        for (TemporalUnit unit : amountToAdd.getUnits()) {
            result = result.plus(amountToAdd.get(unit), unit);
        }
        return result;
    }

    public DateTimeDelta plus(long amount, TemporalUnit unit) {
        if (amount == 0) {
            return this;
        } else if (unit.isTimeBased()) {
            return DateTimeDelta.of(period, duration.plus(amount, unit));
        } else if (unit == ChronoUnit.DAYS) {
            return DateTimeDelta.of(period.plusDays(amount), duration);
        } else if (unit == ChronoUnit.WEEKS) {
            return DateTimeDelta.of(period.plusDays(7 * amount), duration);
        } else if (unit == ChronoUnit.MONTHS) {
            return DateTimeDelta.of(period.plusMonths(amount), duration);
        } else if (unit == ChronoUnit.YEARS) {
            return DateTimeDelta.of(period.plusYears(amount), duration);
        } else if (unit == ChronoUnit.DECADES) {
            return DateTimeDelta.of(period.plusYears(10 * amount), duration);
        } else if (unit == ChronoUnit.CENTURIES) {
            return DateTimeDelta.of(period.plusYears(100 * amount), duration);
        } else if (unit == ChronoUnit.MILLENNIA) {
            return DateTimeDelta.of(period.plusYears(1000 * amount), duration);
        } else if (unit == ChronoUnit.ERAS) {
            return DateTimeDelta.of(period.plusYears(1000_000_000L * amount), duration);
        } else if (unit == ChronoUnit.FOREVER) {
            return amount > 0 ? MAX_VALUE : MIN_VALUE;
        } else {
            throw new UnsupportedTemporalTypeException("Unsupported unit: " + unit);
        }
    }

    public DateTimeDelta minus(TemporalAmount amountToAdd) {
        if (amountToAdd instanceof DateTimeDelta) {
            DateTimeDelta otherDelta = (DateTimeDelta) amountToAdd;
            return DateTimeDelta.of(period.minus(otherDelta.period), duration.minus(otherDelta.duration));
        } else if (amountToAdd instanceof Duration) {
            Duration otherDuration = (Duration) amountToAdd;
            return DateTimeDelta.of(period, duration.minus(otherDuration));
        } else if (amountToAdd instanceof Period) {
            Period otherPeriod = (Period) amountToAdd;
            return DateTimeDelta.of(period.minus(otherPeriod), duration);
        }

        DateTimeDelta result = DateTimeDelta.ZERO;
        for (TemporalUnit unit : amountToAdd.getUnits()) {
            result = result.minus(amountToAdd.get(unit), unit);
        }
        return result;
    }

    public DateTimeDelta minus(long amount, TemporalUnit unit) {
        if (amount == 0) {
            return this;
        } else if (unit.isTimeBased()) {
            return DateTimeDelta.of(period, duration.minus(amount, unit));
        } else if (unit == ChronoUnit.DAYS) {
            return DateTimeDelta.of(period.minusDays(amount), duration);
        } else if (unit == ChronoUnit.WEEKS) {
            return DateTimeDelta.of(period.minusDays(7 * amount), duration);
        } else if (unit == ChronoUnit.MONTHS) {
            return DateTimeDelta.of(period.minusMonths(amount), duration);
        } else if (unit == ChronoUnit.YEARS) {
            return DateTimeDelta.of(period.minusYears(amount), duration);
        } else if (unit == ChronoUnit.DECADES) {
            return DateTimeDelta.of(period.minusYears(10 * amount), duration);
        } else if (unit == ChronoUnit.CENTURIES) {
            return DateTimeDelta.of(period.minusYears(100 * amount), duration);
        } else if (unit == ChronoUnit.MILLENNIA) {
            return DateTimeDelta.of(period.minusYears(1000 * amount), duration);
        } else if (unit == ChronoUnit.ERAS) {
            return DateTimeDelta.of(period.minusYears(1000_000_000L * amount), duration);
        } else if (unit == ChronoUnit.FOREVER) {
            return amount > 0 ? MIN_VALUE : MAX_VALUE;
        } else {
            throw new UnsupportedTemporalTypeException("Unsupported unit: " + unit);
        }
    }

    public Temporal addToWidening(Temporal temporal) {
        return addTo(widenForAddition(temporal));
    }

    public Temporal subtractFromWidening(Temporal temporal) {
        return subtractFrom(widenForAddition(temporal));
    }

    private Temporal widenForAddition(Temporal temporal) {
        if (temporal instanceof LocalDate) {
            if (!duration.isZero()) {
                return ((LocalDate) temporal).atStartOfDay();
            }
        } else if (temporal instanceof LocalTime) {
            if (!period.isZero()) {
                return ((LocalTime) temporal).atDate(LocalDate.ofEpochDay(0));
            }
        } else if (temporal instanceof OffsetTime) {
            if (!period.isZero()) {
                return ((OffsetTime) temporal).atDate(LocalDate.ofEpochDay(0));
            }
        }
        return temporal;
    }

    public DateTimeDelta normalized() {
        Duration normalizedDuration = Duration.ofSeconds(duration.getSeconds() % SECONDS_PER_DAY, duration.getNano());
        Period normalizedPeriod = period.plusDays(duration.toDays()).normalized();
        return of(normalizedPeriod, normalizedDuration);
    }

    public DateTimeDelta collapsed() {
        long rawDays = period.getDays() + duration.toDays();
        int days = (int) (rawDays % ESTIMATED_DAYS_PER_MONTH);
        long rawMonths = period.getMonths() + (rawDays / ESTIMATED_DAYS_PER_MONTH);
        int months = (int) (rawMonths % MONTHS_PER_YEAR);
        int years = period.getYears() + (int) (rawMonths / MONTHS_PER_YEAR);
        int signum = Integer.signum(years);

        if (signum > 0 && months < 0) {
            months += MONTHS_PER_YEAR;
            years -= 1;
        } else if (signum < 0 && months > 0) {
            months -= MONTHS_PER_YEAR;
            years += 1;
        } else if (signum == 0) {
            signum = Integer.signum(months);
        }

        if (signum > 0 && days < 0) {
            days += ESTIMATED_DAYS_PER_MONTH;
            months -= 1;
            if (months < 0) {
                months += MONTHS_PER_YEAR;
                years -= 1;
            }
        } else if (signum < 0 && days > 0) {
            days -= ESTIMATED_DAYS_PER_MONTH;
            months += 1;
            if (months > 0) {
                months -= MONTHS_PER_YEAR;
                years += 1;
            }
        } else if (signum == 0) {
            signum = Integer.signum(days);
        }

        Duration collapsedDuration = Duration.ofSeconds(duration.getSeconds() % SECONDS_PER_DAY, duration.getNano());
        boolean durationIsNegative = collapsedDuration.isNegative();
        if (signum > 0 && durationIsNegative) {
            collapsedDuration = collapsedDuration.plusDays(1);
            days -= 1;
            if (days < 0) {
                days += ESTIMATED_DAYS_PER_MONTH;
                months -= 1;
                if (months < 0) {
                    months += MONTHS_PER_YEAR;
                    years -= 1;
                }
            }
        } else if (signum < 0 && !durationIsNegative && !collapsedDuration.isZero()) {
            collapsedDuration = collapsedDuration.minusDays(1);
            days += 1;
            if (days > 0) {
                days -= ESTIMATED_DAYS_PER_MONTH;
                months += 1;
                if (months > 0) {
                    months -= MONTHS_PER_YEAR;
                    years += 1;
                }
            }
        }

        Period collapsedPeriod = Period.of(years, months, days);

        return of(collapsedPeriod, collapsedDuration);
    }

    public Duration toCollapsedDuration() {
        return duration
                .plusSeconds(period.getDays() * (long) SECONDS_PER_DAY)
                .plusSeconds(period.getMonths() * (long) ESTIMATED_DAYS_PER_MONTH * SECONDS_PER_DAY)
                .plusSeconds(period.getYears() * (long) MONTHS_PER_YEAR * ESTIMATED_DAYS_PER_MONTH * SECONDS_PER_DAY);
    }

    public Duration toDuration() {
        return duration
                .plusSeconds(period.getDays() * (long) SECONDS_PER_DAY)
                .plusSeconds(period.getMonths() * ChronoUnit.MONTHS.getDuration().getSeconds())
                .plusSeconds(period.getYears() * ChronoUnit.YEARS.getDuration().getSeconds());
    }

}
