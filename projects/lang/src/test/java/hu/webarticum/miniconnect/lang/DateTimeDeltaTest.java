package hu.webarticum.miniconnect.lang;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.time.temporal.UnsupportedTemporalTypeException;

import org.junit.jupiter.api.Test;

class DateTimeDeltaTest {

    @Test
    void testGetUnits() {
        DateTimeDelta delta1 = DateTimeDelta.of(Period.of(1, 2, 3), Duration.ofSeconds(4, 5));
        DateTimeDelta delta2 = DateTimeDelta.of(Period.ofMonths(2), Duration.ZERO);
        TemporalUnit[] expectedUnits = new TemporalUnit[] {
                ChronoUnit.YEARS, ChronoUnit.MONTHS, ChronoUnit.DAYS, ChronoUnit.SECONDS, ChronoUnit.NANOS };
        assertThat(delta1.getUnits()).containsExactlyInAnyOrder(expectedUnits);
        assertThat(delta2.getUnits()).containsExactlyInAnyOrder(expectedUnits);
    }

    @Test
    void testZero() {
        assertThat(DateTimeDelta.ZERO.getPeriod()).isEqualTo(Period.ZERO);
        assertThat(DateTimeDelta.ZERO.getDuration()).isEqualTo(Duration.ZERO);
    }

    @Test
    void testCreationDuration() {
        DateTimeDelta delta = DateTimeDelta.of(Duration.ofSeconds(101_000, 324_312_534));
        assertThat(delta.getPeriod()).isEqualTo(Period.ZERO);
        assertThat(delta.getDuration()).isEqualTo(Duration.ofSeconds(101_000, 324_312_534));
    }

    @Test
    void testCreationPeriod() {
        DateTimeDelta delta = DateTimeDelta.of(Period.of(12, 14, 42));
        assertThat(delta.getPeriod()).isEqualTo(Period.of(12, 14, 42));
        assertThat(delta.getDuration()).isEqualTo(Duration.ZERO);
    }

    @Test
    void testCreationPeriodDuration() {
        DateTimeDelta delta = DateTimeDelta.of(Period.of(12, 14, 42), Duration.ofSeconds(101_000, 324_312_534));
        assertThat(delta.getPeriod()).isEqualTo(Period.of(12, 14, 42));
        assertThat(delta.getDuration()).isEqualTo(Duration.ofSeconds(101_000, 324_312_534));
    }

    @Test
    void testCreationUnit() {
        assertThat(DateTimeDelta.of(0, ChronoUnit.MINUTES)).isEqualTo(DateTimeDelta.ZERO);
        assertThat(DateTimeDelta.of(2463, ChronoUnit.NANOS)).isEqualTo(DateTimeDelta.of(0, 0, 0, 0, 2463));
        assertThat(DateTimeDelta.of(-2_436_400_014L, ChronoUnit.NANOS)).isEqualTo(DateTimeDelta.of(0, 0, 0, -3, 563_599_986));
    }

    @Test
    void testCreationPeriodYearsToSeconds() {
        DateTimeDelta delta = DateTimeDelta.of(7, 25, 67, 256_000);
        assertThat(delta.getPeriod()).isEqualTo(Period.of(7, 25, 67));
        assertThat(delta.getDuration()).isEqualTo(Duration.ofSeconds(256_000));
    }
    
    @Test
    void testCreationPeriodYearsToNanos() {
        DateTimeDelta delta = DateTimeDelta.of(10, 32, 42, 178_091, 123_456);
        assertThat(delta.getPeriod()).isEqualTo(Period.of(10, 32, 42));
        assertThat(delta.getDuration()).isEqualTo(Duration.ofSeconds(178_091, 123_456));
    }

    @Test
    void testCreationFrom() {
        assertThat(DateTimeDelta.from(Duration.ofSeconds(1, 2))).isEqualTo(DateTimeDelta.of(0, 0, 0, 1, 2));
        assertThat(DateTimeDelta.from(Period.of(7, -8, 9))).isEqualTo(DateTimeDelta.of(7, -8, 9, 0, 0));
        assertThat(DateTimeDelta.of(9, -8, -7, 6, 5)).isEqualTo(DateTimeDelta.of(9, -8, -7, 6, 5));
    }

    @Test
    void testCreationBetweenSameType() {
        assertThat(DateTimeDelta.between(
                LocalDateTime.of(2026, 4, 10, 14, 9, 3, 43_132_111),
                LocalDateTime.of(2025, 2, 9, 12, 9, 1, 43_072_299)))
                        .isEqualTo(DateTimeDelta.of(-1, -2, -1, -7202, -59_812));
        assertThat(DateTimeDelta.between(
                Instant.ofEpochSecond(1763460521, 43_072_199),
                Instant.ofEpochSecond(1763461203, 43_132_011)))
                        .isEqualTo(DateTimeDelta.of(0, 0, 0, 682, 59_812));
        assertThat(DateTimeDelta.between(
                LocalDate.of(2022, 2, 4),
                LocalDate.of(2020, 8, 1)))
                        .isEqualTo(DateTimeDelta.of(-1, -6, -3, 0, 0));
        assertThat(DateTimeDelta.between(
                OffsetTime.of(13, 1, 43, 412_351_233, ZoneOffset.UTC),
                OffsetTime.of(17, 0, 55, 670_124_972, ZoneOffset.UTC)))
                        .isEqualTo(DateTimeDelta.of(0, 0, 0, 14352, 257_773_739));
        assertThat(DateTimeDelta.between(
                LocalTime.of(1, 2, 3, 4),
                LocalTime.of(0, 1, 2, 3)))
                        .isEqualTo(DateTimeDelta.of(0, 0, 0, -3661, -1));
    }

    @Test
    void testCreationBetweenSameTypeDecreasingTime() {
        assertThat(DateTimeDelta.between(
                ZonedDateTime.of(2023, 7, 4, 13, 9, 10, 7, ZoneId.of("Europe/Budapest")),
                ZonedDateTime.of(2025, 2, 9, 11, 9, 10, 132, ZoneId.of("Europe/Budapest"))))
                        .isEqualTo(DateTimeDelta.of(1, 7, 4, 82800, 125));
        assertThat(DateTimeDelta.between(
                OffsetDateTime.of(2023, 1, 4, 13, 9, 10, 7, ZoneOffset.UTC),
                OffsetDateTime.of(2025, 2, 9, 11, 9, 10, 132, ZoneOffset.UTC)))
                        .isEqualTo(DateTimeDelta.of(2, 1, 4, 79200, 125));
        assertThat(DateTimeDelta.between(
                OffsetDateTime.of(2025, 11, 12, 8, 30, 0, 0, ZoneOffset.UTC),
                OffsetDateTime.of(2025, 11, 14, 9, 10, 0, 0, ZoneOffset.ofHours(1))))
                        .isEqualTo(DateTimeDelta.of(0, 0, 1, 85200, 0));
    }

    @Test
    void testCreationBetweenSameTypeDayChangerOffset() {
        assertThat(DateTimeDelta.between(
                OffsetDateTime.of(2025, 11, 12, 8, 30, 0, 0, ZoneOffset.UTC),
                OffsetDateTime.of(2025, 11, 14, 0, 30, 0, 0, ZoneOffset.ofHours(1))))
                        .isEqualTo(DateTimeDelta.of(0, 0, 1, 54000, 0));
    }

    @Test
    void testCreationBetweenSameTypeBigOffset() {
        assertThat(DateTimeDelta.between(
                OffsetDateTime.of(2025, 11, 12, 11, 0, 0, 0, ZoneOffset.ofHours(-15)),
                OffsetDateTime.of(2025, 11, 12, 13, 0, 0, 0, ZoneOffset.ofHours(15))))
                        .isEqualTo(DateTimeDelta.of(0, 0, -1, -14400, 0));
        assertThat(DateTimeDelta.between(
                OffsetDateTime.of(2025, 11, 12, 13, 0, 0, 0, ZoneOffset.ofHours(15)),
                OffsetDateTime.of(2025, 11, 12, 11, 0, 0, 0, ZoneOffset.ofHours(-15))))
                        .isEqualTo(DateTimeDelta.of(0, 0, 1, 14400, 0));
    }

    @Test
    void testCreationBetweenSomeDifferentTypes() {
        assertThat(DateTimeDelta.between(
                Instant.ofEpochSecond(1763460521, 43_072_199),
                OffsetDateTime.of(2025, 11, 22, 10, 9, 52, 50_000_000, ZoneOffset.UTC)))
                        .isEqualTo(DateTimeDelta.of(0, 0, 4, 71, 6_927_801));
        assertThat(DateTimeDelta.between(
                LocalDate.of(2022, 2, 4),
                OffsetDateTime.of(2023, 1, 4, 13, 9, 10, 7, ZoneOffset.UTC)))
                        .isEqualTo(DateTimeDelta.of(0, 11, 0, 47350, 7));
        assertThat(DateTimeDelta.between(
                OffsetTime.of(2, 1, 43, 412_351_233, ZoneOffset.ofHours(2)),
                LocalTime.of(1, 0, 13, 100_000_000)))
                        .isEqualTo(DateTimeDelta.of(0, 0, 0, -3690, -312_351_233));
        assertThat(DateTimeDelta.between(
                LocalTime.of(13, 42, 15, 123),
                LocalDateTime.of(2025, 5, 11, 11, 42, 15, 3)))
                        .isEqualTo(DateTimeDelta.of(0, 0, 0, -7200, -120));
    }

    @Test
    void testParseInvalid() {
        assertThatThrownBy(() -> DateTimeDelta.parse(null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> DateTimeDelta.parse("")).isInstanceOf(DateTimeParseException.class);
        assertThatThrownBy(() -> DateTimeDelta.parse("lorem")).isInstanceOf(DateTimeParseException.class);
        assertThatThrownBy(() -> DateTimeDelta.parse(".-23")).isInstanceOf(DateTimeParseException.class);
        assertThatThrownBy(() -> DateTimeDelta.parse("0.42.94")).isInstanceOf(DateTimeParseException.class);
        assertThatThrownBy(() -> DateTimeDelta.parse("-+5")).isInstanceOf(DateTimeParseException.class);
        assertThatThrownBy(() -> DateTimeDelta.parse("P3S")).isInstanceOf(DateTimeParseException.class);
        assertThatThrownBy(() -> DateTimeDelta.parse("P-T3S")).isInstanceOf(DateTimeParseException.class);
        assertThatThrownBy(() -> DateTimeDelta.parse("PT2Y")).isInstanceOf(DateTimeParseException.class);
        assertThatThrownBy(() -> DateTimeDelta.parse("PT2Y")).isInstanceOf(DateTimeParseException.class);
        assertThatThrownBy(() -> DateTimeDelta.parse("12:22.55")).isInstanceOf(DateTimeParseException.class);
        assertThatThrownBy(() -> DateTimeDelta.parse("12-32-13-21")).isInstanceOf(DateTimeParseException.class);
        assertThatThrownBy(() -> DateTimeDelta.parse("34 12:41.7")).isInstanceOf(DateTimeParseException.class);
        assertThatThrownBy(() -> DateTimeDelta.parse("7-34 12:31")).isInstanceOf(DateTimeParseException.class);
        assertThatThrownBy(() -> DateTimeDelta.parse("1 2 3")).isInstanceOf(DateTimeParseException.class);
        assertThatThrownBy(() -> DateTimeDelta.parse("12 days ago ago")).isInstanceOf(DateTimeParseException.class);
        assertThatThrownBy(() -> DateTimeDelta.parse("@")).isInstanceOf(DateTimeParseException.class);
    }

    @Test
    void testParseSeconds() {
        assertThat(DateTimeDelta.parse("34")).isEqualTo(DateTimeDelta.of(0, 0, 0, 34, 0));
        assertThat(DateTimeDelta.parse("97353")).isEqualTo(DateTimeDelta.of(0, 0, 0, 97353, 0));
        assertThat(DateTimeDelta.parse("52.")).isEqualTo(DateTimeDelta.of(0, 0, 0, 52, 0));
        assertThat(DateTimeDelta.parse("+13")).isEqualTo(DateTimeDelta.of(0, 0, 0, 13, 0));
        assertThat(DateTimeDelta.parse("+94.")).isEqualTo(DateTimeDelta.of(0, 0, 0, 94, 0));
        assertThat(DateTimeDelta.parse("+560718")).isEqualTo(DateTimeDelta.of(0, 0, 0, 560718, 0));
        assertThat(DateTimeDelta.parse("-7")).isEqualTo(DateTimeDelta.of(0, 0, 0, -7, 0));
        assertThat(DateTimeDelta.parse("-18.")).isEqualTo(DateTimeDelta.of(0, 0, 0, -18, 0));
        assertThat(DateTimeDelta.parse("-397485")).isEqualTo(DateTimeDelta.of(0, 0, 0, -397485, 0));
        assertThat(DateTimeDelta.parse(".123")).isEqualTo(DateTimeDelta.of(0, 0, 0, 0, 123_000_000));
        assertThat(DateTimeDelta.parse(".0123")).isEqualTo(DateTimeDelta.of(0, 0, 0, 0, 12_300_000));
        assertThat(DateTimeDelta.parse("+.42")).isEqualTo(DateTimeDelta.of(0, 0, 0, 0, 420_000_000));
        assertThat(DateTimeDelta.parse("+.0089683")).isEqualTo(DateTimeDelta.of(0, 0, 0, 0, 8_968_300));
        assertThat(DateTimeDelta.parse("-.38")).isEqualTo(DateTimeDelta.of(0, 0, 0, 0, -380_000_000));
        assertThat(DateTimeDelta.parse("-.01364")).isEqualTo(DateTimeDelta.of(0, 0, 0, 0, -13_640_000));
        assertThat(DateTimeDelta.parse("5.35260")).isEqualTo(DateTimeDelta.of(0, 0, 0, 5, 352_600_000));
        assertThat(DateTimeDelta.parse("12.04389")).isEqualTo(DateTimeDelta.of(0, 0, 0, 12, 43_890_000));
        assertThat(DateTimeDelta.parse("+42.125090")).isEqualTo(DateTimeDelta.of(0, 0, 0, 42, 125_090_000));
        assertThat(DateTimeDelta.parse("+73.00012")).isEqualTo(DateTimeDelta.of(0, 0, 0, 73, 120_000));
        assertThat(DateTimeDelta.parse("-47.3207")).isEqualTo(DateTimeDelta.of(0, 0, 0, -47, -320_700_000));
        assertThat(DateTimeDelta.parse("-51.04260734")).isEqualTo(DateTimeDelta.of(0, 0, 0, -51, -42607340));
        assertThat(DateTimeDelta.parse("@11")).isEqualTo(DateTimeDelta.of(0, 0, 0, 11, 0));
        assertThat(DateTimeDelta.parse("@7.9")).isEqualTo(DateTimeDelta.of(0, 0, 0, 7, 900_000_000));
        assertThat(DateTimeDelta.parse("@-.2")).isEqualTo(DateTimeDelta.of(0, 0, 0,0, -200_000_000));
        assertThat(DateTimeDelta.parse("@+1.0012")).isEqualTo(DateTimeDelta.of(0, 0, 0, 1, 1_200_000));
    }

    @Test
    void testParseIso8601Unsigned() {
        assertThat(DateTimeDelta.parse("PT0S")).isEqualTo(DateTimeDelta.ZERO);
        assertThat(DateTimeDelta.parse("PT0.000S")).isEqualTo(DateTimeDelta.ZERO);
        assertThat(DateTimeDelta.parse("PT132S")).isEqualTo(DateTimeDelta.of(0, 0, 0, 132, 0));
        assertThat(DateTimeDelta.parse("PT132S")).isEqualTo(DateTimeDelta.of(0, 0, 0, 132, 0));
        assertThat(DateTimeDelta.parse("PT12.41S")).isEqualTo(DateTimeDelta.of(0, 0, 0, 12, 410_000_000));
        assertThat(DateTimeDelta.parse("PT0.000005208S")).isEqualTo(DateTimeDelta.of(0, 0, 0, 0, 5208));
        assertThat(DateTimeDelta.parse("PT0M")).isEqualTo(DateTimeDelta.ZERO);
        assertThat(DateTimeDelta.parse("PT4M")).isEqualTo(DateTimeDelta.of(0, 0, 0, 240, 0));
        assertThat(DateTimeDelta.parse("PT256M")).isEqualTo(DateTimeDelta.of(0, 0, 0, 15360, 0));
        assertThat(DateTimeDelta.parse("PT0H")).isEqualTo(DateTimeDelta.ZERO);
        assertThat(DateTimeDelta.parse("PT2H")).isEqualTo(DateTimeDelta.of(0, 0, 0, 7200, 0));
        assertThat(DateTimeDelta.parse("PT35H")).isEqualTo(DateTimeDelta.of(0, 0, 0, 126000, 0));
        assertThat(DateTimeDelta.parse("P0D")).isEqualTo(DateTimeDelta.ZERO);
        assertThat(DateTimeDelta.parse("P3D")).isEqualTo(DateTimeDelta.of(0, 0, 3, 0, 0));
        assertThat(DateTimeDelta.parse("P58D")).isEqualTo(DateTimeDelta.of(0, 0, 58, 0, 0));
        assertThat(DateTimeDelta.parse("P0M")).isEqualTo(DateTimeDelta.ZERO);
        assertThat(DateTimeDelta.parse("P7M")).isEqualTo(DateTimeDelta.of(0, 7, 0, 0, 0));
        assertThat(DateTimeDelta.parse("P85M")).isEqualTo(DateTimeDelta.of(0, 85, 0, 0, 0));
        assertThat(DateTimeDelta.parse("P0Y")).isEqualTo(DateTimeDelta.ZERO);
        assertThat(DateTimeDelta.parse("P42Y")).isEqualTo(DateTimeDelta.of(42, 0, 0, 0, 0));
        assertThat(DateTimeDelta.parse("PT7M3S")).isEqualTo(DateTimeDelta.of(0, 0, 0, 423, 0));
        assertThat(DateTimeDelta.parse("PT2H0.04S")).isEqualTo(DateTimeDelta.of(0, 0, 0, 7200, 40_000_000));
        assertThat(DateTimeDelta.parse("PT5H12M32S")).isEqualTo(DateTimeDelta.of(0, 0, 0, 18752, 0));
        assertThat(DateTimeDelta.parse("PT3H42M1.124354S")).isEqualTo(DateTimeDelta.of(0, 0, 0, 13321, 124_354_000));
        assertThat(DateTimeDelta.parse("P3Y34D")).isEqualTo(DateTimeDelta.of(3, 0, 34, 0, 0));
        assertThat(DateTimeDelta.parse("P7Y20M")).isEqualTo(DateTimeDelta.of(7, 20, 0, 0, 0));
        assertThat(DateTimeDelta.parse("P92M63D")).isEqualTo(DateTimeDelta.of(0, 92, 63, 0, 0));
        assertThat(DateTimeDelta.parse("P1Y3M10D")).isEqualTo(DateTimeDelta.of(1, 3, 10, 0, 0));
        assertThat(DateTimeDelta.parse("P15Y25M91D")).isEqualTo(DateTimeDelta.of(15, 25, 91, 0, 0));
        assertThat(DateTimeDelta.parse("P8MT40M")).isEqualTo(DateTimeDelta.of(0, 8, 0, 2400, 0));
        assertThat(DateTimeDelta.parse("P1DT0M12.999S")).isEqualTo(DateTimeDelta.of(0, 0, 1, 12, 999_000_000));
        assertThat(DateTimeDelta.parse("P9Y55MT8H")).isEqualTo(DateTimeDelta.of(9, 55, 0, 28800, 0));
        assertThat(DateTimeDelta.parse("P90Y2M80DT3H40M12.009S")).isEqualTo(DateTimeDelta.of(90, 2, 80, 13212, 9_000_000));
        assertThat(DateTimeDelta.parse("p90Y2M80Dt3H40M12.009s")).isEqualTo(DateTimeDelta.of(90, 2, 80, 13212, 9_000_000));
    }

    @Test
    void testParseIso8601Signed() {
        assertThat(DateTimeDelta.parse("PT-2S")).isEqualTo(DateTimeDelta.of(0, 0, 0, -2, 0));
        assertThat(DateTimeDelta.parse("PT+15S")).isEqualTo(DateTimeDelta.of(0, 0, 0, 15, 0));
        assertThat(DateTimeDelta.parse("PT-23.42538S")).isEqualTo(DateTimeDelta.of(0, 0, 0, -23, -425_380_000));
        assertThat(DateTimeDelta.parse("PT7H-35M9.1S")).isEqualTo(DateTimeDelta.of(0, 0, 0, 23109, 100_000_000));
        assertThat(DateTimeDelta.parse("PT-7H-35M+9.1S")).isEqualTo(DateTimeDelta.of(0, 0, 0, -27290, -900_000_000));
        assertThat(DateTimeDelta.parse("-PT7H-35M9.1S")).isEqualTo(DateTimeDelta.of(0, 0, 0, -23109, -100_000_000));
        assertThat(DateTimeDelta.parse("P-1D")).isEqualTo(DateTimeDelta.of(0, 0, -1, 0, 0));
        assertThat(DateTimeDelta.parse("-P2D")).isEqualTo(DateTimeDelta.of(0, 0, -2, 0, 0));
        assertThat(DateTimeDelta.parse("-P-3D")).isEqualTo(DateTimeDelta.of(0, 0, 3, 0, 0));
        assertThat(DateTimeDelta.parse("P+4D")).isEqualTo(DateTimeDelta.of(0, 0, 4, 0, 0));
        assertThat(DateTimeDelta.parse("-P+5D")).isEqualTo(DateTimeDelta.of(0, 0, -5, 0, 0));
        assertThat(DateTimeDelta.parse("P-56D")).isEqualTo(DateTimeDelta.of(0, 0, -56, 0, 0));
        assertThat(DateTimeDelta.parse("P-9M")).isEqualTo(DateTimeDelta.of(0, -9, 0, 0, 0));
        assertThat(DateTimeDelta.parse("P-12Y")).isEqualTo(DateTimeDelta.of(-12, 0, 0, 0, 0));
        assertThat(DateTimeDelta.parse("P2Y-1D")).isEqualTo(DateTimeDelta.of(2, 0, -1, 0, 0));
        assertThat(DateTimeDelta.parse("-P2Y-1D")).isEqualTo(DateTimeDelta.of(-2, 0, 1, 0, 0));
        assertThat(DateTimeDelta.parse("P+2Y-1M3D")).isEqualTo(DateTimeDelta.of(2, -1, 3, 0, 0));
        assertThat(DateTimeDelta.parse("-P+2Y-1M3D")).isEqualTo(DateTimeDelta.of(-2, 1, -3, 0, 0));
        assertThat(DateTimeDelta.parse("P2Y-7DT3H2M")).isEqualTo(DateTimeDelta.of(2, 0, -7, 10920, 0));
        assertThat(DateTimeDelta.parse("P8MT-9M3.12S")).isEqualTo(DateTimeDelta.of(0, 8, 0, -536, -880_000_000));
        assertThat(DateTimeDelta.parse("P+6Y1M-3DT2H+8M-19.0234S")).isEqualTo(DateTimeDelta.of(6, 1, -3, 7660, 976_600_000));
        assertThat(DateTimeDelta.parse("-P+6Y1M-3DT2H+8M-19.0234S")).isEqualTo(DateTimeDelta.of(-6, -1, 3, -7660, -976_600_000));
    }

    @Test
    void testParseSqlIntervalPair() {
        assertThat(DateTimeDelta.parse("12:34")).isEqualTo(DateTimeDelta.of(0, 0, 0, 45240, 0));
        assertThat(DateTimeDelta.parse("3 5")).isEqualTo(DateTimeDelta.of(0, 3, 5, 0, 0));
        assertThat(DateTimeDelta.parse("12-8")).isEqualTo(DateTimeDelta.of(12, 8, 0, 0, 0));
    }

    @Test
    void testParseSqlIntervalTriplet() {
        assertThat(DateTimeDelta.parse("1:1:01")).isEqualTo(DateTimeDelta.of(0, 0, 0, 3661, 0));
        assertThat(DateTimeDelta.parse("12:34:12")).isEqualTo(DateTimeDelta.of(0, 0, 0, 45252, 0));
        assertThat(DateTimeDelta.parse("12:34:15.")).isEqualTo(DateTimeDelta.of(0, 0, 0, 45255, 0));
        assertThat(DateTimeDelta.parse("12:34:.0123")).isEqualTo(DateTimeDelta.of(0, 0, 0, 45240, 12_300_000));
        assertThat(DateTimeDelta.parse("12:34:11.034")).isEqualTo(DateTimeDelta.of(0, 0, 0, 45251, 34_000_000));
        assertThat(DateTimeDelta.parse("5 3:02")).isEqualTo(DateTimeDelta.of(0, 0, 5, 10920, 0));
        assertThat(DateTimeDelta.parse("1-2 3")).isEqualTo(DateTimeDelta.of(1, 2, 3, 0, 0));
    }

    @Test
    void testParseSqlIntervalQuadruplet() {
        assertThat(DateTimeDelta.parse("7 1:1:01")).isEqualTo(DateTimeDelta.of(0, 0, 7, 3661, 0));
        assertThat(DateTimeDelta.parse("8 12:34:12")).isEqualTo(DateTimeDelta.of(0, 0, 8, 45252, 0));
        assertThat(DateTimeDelta.parse("9 12:34:15.")).isEqualTo(DateTimeDelta.of(0, 0, 9, 45255, 0));
        assertThat(DateTimeDelta.parse("10 12:34:.0123")).isEqualTo(DateTimeDelta.of(0, 0, 10, 45240, 12_300_000));
        assertThat(DateTimeDelta.parse("11 12:34:11.034")).isEqualTo(DateTimeDelta.of(0, 0, 11, 45251, 34_000_000));
        assertThat(DateTimeDelta.parse("1 2 03:4")).isEqualTo(DateTimeDelta.of(0, 1, 2, 11040, 0));
    }

    @Test
    void testParseSqlIntervalQuintuplet() {
        assertThat(DateTimeDelta.parse("1 7 1:1:01")).isEqualTo(DateTimeDelta.of(0, 1, 7, 3661, 0));
        assertThat(DateTimeDelta.parse("2 8 12:34:12")).isEqualTo(DateTimeDelta.of(0, 2, 8, 45252, 0));
        assertThat(DateTimeDelta.parse("3 9 12:34:15.")).isEqualTo(DateTimeDelta.of(0, 3, 9, 45255, 0));
        assertThat(DateTimeDelta.parse("4 10 12:34:.0123")).isEqualTo(DateTimeDelta.of(0, 4, 10, 45240, 12_300_000));
        assertThat(DateTimeDelta.parse("5 11 12:34:11.034")).isEqualTo(DateTimeDelta.of(0, 5, 11, 45251, 34_000_000));
        assertThat(DateTimeDelta.parse("23-07 2 0:09")).isEqualTo(DateTimeDelta.of(23, 7, 2, 540, 0));
    }

    @Test
    void testParseSqlIntervalFull() {
        assertThat(DateTimeDelta.parse("0-1 7 1:1:01")).isEqualTo(DateTimeDelta.of(0, 1, 7, 3661, 0));
        assertThat(DateTimeDelta.parse("3-2 8 12:34:12")).isEqualTo(DateTimeDelta.of(3, 2, 8, 45252, 0));
        assertThat(DateTimeDelta.parse("6-3 9 12:34:15.")).isEqualTo(DateTimeDelta.of(6, 3, 9, 45255, 0));
        assertThat(DateTimeDelta.parse("9-4 10 12:34:.0123")).isEqualTo(DateTimeDelta.of(9, 4, 10, 45240, 12_300_000));
        assertThat(DateTimeDelta.parse("12-5 11 12:34:11.034")).isEqualTo(DateTimeDelta.of(12, 5, 11, 45251, 34_000_000));
    }

    @Test
    void testParseSqlIntervalAgo() {
        assertThat(DateTimeDelta.parse("12:34 ago")).isEqualTo(DateTimeDelta.of(0, 0, 0, -45240, 0));
        assertThat(DateTimeDelta.parse("12:34:11.034 ago")).isEqualTo(DateTimeDelta.of(0, 0, 0, -45251, -34_000_000));
        assertThat(DateTimeDelta.parse("12-8 ago")).isEqualTo(DateTimeDelta.of(-12, -8, 0, 0, 0));
        assertThat(DateTimeDelta.parse("10 12:34:.0123 ago")).isEqualTo(DateTimeDelta.of(0, 0, -10, -45240, -12_300_000));
        assertThat(DateTimeDelta.parse("23-07 2 0:09 ago")).isEqualTo(DateTimeDelta.of(-23, -7, -2, -540, 0));
        assertThat(DateTimeDelta.parse("0-1 7 1:1:01 ago")).isEqualTo(DateTimeDelta.of(0, -1, -7, -3661, 0));
        assertThat(DateTimeDelta.parse("9-4 10 12:34:.0123 ago")).isEqualTo(DateTimeDelta.of(-9, -4, -10, -45240, -12_300_000));
    }

    @Test
    void testParseSqlIntervalAt() {
        assertThat(DateTimeDelta.parse("@12:34")).isEqualTo(DateTimeDelta.of(0, 0, 0, 45240, 0));
        assertThat(DateTimeDelta.parse("@ 1:1:01")).isEqualTo(DateTimeDelta.of(0, 0, 0, 3661, 0));
        assertThat(DateTimeDelta.parse("@12:34 ago")).isEqualTo(DateTimeDelta.of(0, 0, 0, -45240, 0));
        assertThat(DateTimeDelta.parse("@ 23-07 2 0:09 ago")).isEqualTo(DateTimeDelta.of(-23, -7, -2, -540, 0));
        assertThat(DateTimeDelta.parse("@0-1 7 1:1:01")).isEqualTo(DateTimeDelta.of(0, 1, 7, 3661, 0));
        assertThat(DateTimeDelta.parse("@ 9-4 10 12:34:.0123 ago")).isEqualTo(DateTimeDelta.of(-9, -4, -10, -45240, -12_300_000));
    }

    @Test
    void testParsePgVerboseDaysVariants() {
        assertThat(DateTimeDelta.parse("1 day")).isEqualTo(DateTimeDelta.of(0, 0, 1, 0, 0));
        assertThat(DateTimeDelta.parse("3 day")).isEqualTo(DateTimeDelta.of(0, 0, 3, 0, 0));
        assertThat(DateTimeDelta.parse("10 day")).isEqualTo(DateTimeDelta.of(0, 0, 10, 0, 0));
        assertThat(DateTimeDelta.parse("12 days")).isEqualTo(DateTimeDelta.of(0, 0, 12, 0, 0));
        assertThat(DateTimeDelta.parse("-4 day")).isEqualTo(DateTimeDelta.of(0, 0, -4, 0, 0));
        assertThat(DateTimeDelta.parse("8days")).isEqualTo(DateTimeDelta.of(0, 0, 8, 0, 0));
        assertThat(DateTimeDelta.parse("- 9day")).isEqualTo(DateTimeDelta.of(0, 0, -9, 0, 0));
        assertThat(DateTimeDelta.parse("-11days")).isEqualTo(DateTimeDelta.of(0, 0, -11, 0, 0));
        assertThat(DateTimeDelta.parse("@ 3 days")).isEqualTo(DateTimeDelta.of(0, 0, 3, 0, 0));
        assertThat(DateTimeDelta.parse("@ -5 days")).isEqualTo(DateTimeDelta.of(0, 0, -5, 0, 0));
        assertThat(DateTimeDelta.parse(" @ - 1 days ")).isEqualTo(DateTimeDelta.of(0, 0, -1, 0, 0));
        assertThat(DateTimeDelta.parse(" @- 432 days  ")).isEqualTo(DateTimeDelta.of(0, 0, -432, 0, 0));
    }

    @Test
    void testParsePgVerboseDaysAgoVariants() {
        assertThat(DateTimeDelta.parse("1 day ago")).isEqualTo(DateTimeDelta.of(0, 0, -1, 0, 0));
        assertThat(DateTimeDelta.parse("3 day ago")).isEqualTo(DateTimeDelta.of(0, 0, -3, 0, 0));
        assertThat(DateTimeDelta.parse("10 day ago")).isEqualTo(DateTimeDelta.of(0, 0, -10, 0, 0));
        assertThat(DateTimeDelta.parse("12 days ago")).isEqualTo(DateTimeDelta.of(0, 0, -12, 0, 0));
        assertThat(DateTimeDelta.parse("-4 day ago")).isEqualTo(DateTimeDelta.of(0, 0, 4, 0, 0));
        assertThat(DateTimeDelta.parse("8days ago")).isEqualTo(DateTimeDelta.of(0, 0, -8, 0, 0));
        assertThat(DateTimeDelta.parse("- 9day ago")).isEqualTo(DateTimeDelta.of(0, 0, 9, 0, 0));
        assertThat(DateTimeDelta.parse("-11days ago")).isEqualTo(DateTimeDelta.of(0, 0, 11, 0, 0));
        assertThat(DateTimeDelta.parse("@ 3 days ago")).isEqualTo(DateTimeDelta.of(0, 0, -3, 0, 0));
        assertThat(DateTimeDelta.parse("@ -5 days ago")).isEqualTo(DateTimeDelta.of(0, 0, 5, 0, 0));
        assertThat(DateTimeDelta.parse(" @ - 1 days ago ")).isEqualTo(DateTimeDelta.of(0, 0, 1, 0, 0));
        assertThat(DateTimeDelta.parse(" @- 432 days   ago    ")).isEqualTo(DateTimeDelta.of(0, 0, 432, 0, 0));
    }

    @Test
    void testParsePgVerboseVarious() {
        assertThat(DateTimeDelta.parse("12 years ago")).isEqualTo(DateTimeDelta.of(-12, 0, 0, 0, 0));
        assertThat(DateTimeDelta.parse("2 days 1 hour")).isEqualTo(DateTimeDelta.of(0, 0, 2, 3600, 0));
        assertThat(DateTimeDelta.parse("1 hours - 30 minute")).isEqualTo(DateTimeDelta.of(0, 0, 0, 1800, 0));
        assertThat(DateTimeDelta.parse("@ 2 weeks -3hours 1minute")).isEqualTo(DateTimeDelta.of(0, 0, 14, -10740, 0));
        assertThat(DateTimeDelta.parse("1 days -3nanos ago")).isEqualTo(DateTimeDelta.of(0, 0, -1, 0, 3));
        assertThat(DateTimeDelta.parse("2 months -2 hours 2millis")).isEqualTo(DateTimeDelta.of(0, 2, 0, -7199, -998_000_000));
        assertThat(DateTimeDelta.parse("1 decade -1 microsecond")).isEqualTo(DateTimeDelta.of(10, 0, 0, 0, -1_000));
        assertThat(DateTimeDelta.parse("@2 centuries - 1 year + 8 seconds")).isEqualTo(DateTimeDelta.of(199, 0, 0, 8, 0));
        assertThat(DateTimeDelta.parse(" @ 1 era -999992millennia + 5 weeks - 1second ago ")).isEqualTo(DateTimeDelta.of(-8000, 0, -35, 1, 0));
    }

    @Test
    void testGetZero() {
        assertThat(DateTimeDelta.ZERO.get(ChronoUnit.NANOS)).isZero();
        assertThat(DateTimeDelta.ZERO.get(ChronoUnit.SECONDS)).isZero();
        assertThat(DateTimeDelta.ZERO.get(ChronoUnit.DAYS)).isZero();
        assertThat(DateTimeDelta.ZERO.get(ChronoUnit.MONTHS)).isZero();
        assertThat(DateTimeDelta.ZERO.get(ChronoUnit.YEARS)).isZero();
        assertThatThrownBy(() -> DateTimeDelta.ZERO.get(ChronoUnit.MINUTES)).isInstanceOf(UnsupportedTemporalTypeException.class);
        assertThatThrownBy(() -> DateTimeDelta.ZERO.get(ChronoUnit.HOURS)).isInstanceOf(UnsupportedTemporalTypeException.class);
        assertThatThrownBy(() -> DateTimeDelta.ZERO.get(ChronoUnit.CENTURIES)).isInstanceOf(UnsupportedTemporalTypeException.class);
    }

    @Test
    void testGetNonZero() {
        DateTimeDelta delta = DateTimeDelta.of(2, -3, 1, 46273, 123_456_789);
        assertThat(delta.get(ChronoUnit.NANOS)).isEqualTo(123_456_789);
        assertThat(delta.get(ChronoUnit.SECONDS)).isEqualTo(46273);
        assertThat(delta.get(ChronoUnit.DAYS)).isEqualTo(1);
        assertThat(delta.get(ChronoUnit.MONTHS)).isEqualTo(-3);
        assertThat(delta.get(ChronoUnit.YEARS)).isEqualTo(2);
        assertThatThrownBy(() -> delta.get(ChronoUnit.MINUTES)).isInstanceOf(UnsupportedTemporalTypeException.class);
        assertThatThrownBy(() -> delta.get(ChronoUnit.HOURS)).isInstanceOf(UnsupportedTemporalTypeException.class);
        assertThatThrownBy(() -> delta.get(ChronoUnit.CENTURIES)).isInstanceOf(UnsupportedTemporalTypeException.class);
    }

    @Test
    void testAddToLocalTime() {
        LocalTime baseTime = LocalTime.of(7, 12, 41, 324_500_000);
        assertThat(DateTimeDelta.ZERO.addTo(baseTime)).isEqualTo(baseTime);
        assertThat(DateTimeDelta.of(0, 0, 0, 1, 0).addTo(baseTime)).isEqualTo(LocalTime.of(7, 12, 42, 324_500_000));
        assertThat(DateTimeDelta.of(0, 0, 0, 46273, 123_456_789).addTo(baseTime)).isEqualTo(LocalTime.of(20, 3, 54, 447_956_789));
        assertThat(DateTimeDelta.of(0, 0, 0, 73029, 0).addTo(baseTime)).isEqualTo(LocalTime.of(3, 29, 50, 324_500_000));
        assertThat(DateTimeDelta.of(0, 0, 0, -425, 0).addTo(baseTime)).isEqualTo(LocalTime.of(7, 5, 36, 324_500_000));
        assertThatThrownBy(() -> DateTimeDelta.of(1, 0, 0, 0, 0).addTo(baseTime)).isInstanceOf(UnsupportedTemporalTypeException.class);
        assertThatThrownBy(() -> DateTimeDelta.of(0, 1, 2, 24, 12_000_000).addTo(baseTime)).isInstanceOf(UnsupportedTemporalTypeException.class);
    }

    @Test
    void testAddToOffsetTime() {
        ZoneOffset offset = ZoneOffset.ofHours(2);
        OffsetTime baseTime = OffsetTime.of(7, 12, 41, 324_500_000, offset);
        assertThat(DateTimeDelta.ZERO.addTo(baseTime)).isEqualTo(baseTime);
        assertThat(DateTimeDelta.of(0, 0, 0, 1, 0).addTo(baseTime)).isEqualTo(OffsetTime.of(7, 12, 42, 324_500_000, offset));
        assertThat(DateTimeDelta.of(0, 0, 0, 46273, 123_456_789).addTo(baseTime)).isEqualTo(OffsetTime.of(20, 3, 54, 447_956_789, offset));
        assertThat(DateTimeDelta.of(0, 0, 0, 73029, 0).addTo(baseTime)).isEqualTo(OffsetTime.of(3, 29, 50, 324_500_000, offset));
        assertThat(DateTimeDelta.of(0, 0, 0, -425, 0).addTo(baseTime)).isEqualTo(OffsetTime.of(7, 5, 36, 324_500_000, offset));
        assertThatThrownBy(() -> DateTimeDelta.of(1, 0, 0, 0, 0).addTo(baseTime)).isInstanceOf(UnsupportedTemporalTypeException.class);
        assertThatThrownBy(() -> DateTimeDelta.of(0, 1, 2, 24, 12_000_000).addTo(baseTime)).isInstanceOf(UnsupportedTemporalTypeException.class);
    }

    @Test
    void testAddToLocalDate() {
        LocalDate baseDate = LocalDate.of(2025, 1, 15);
        assertThat(DateTimeDelta.ZERO.addTo(baseDate)).isEqualTo(baseDate);
        assertThat(DateTimeDelta.of(1, 0, 0, 0, 0).addTo(baseDate)).isEqualTo(LocalDate.of(2026, 1, 15));
        assertThat(DateTimeDelta.of(0, 0, 20, 0, 0).addTo(baseDate)).isEqualTo(LocalDate.of(2025, 2, 4));
        assertThat(DateTimeDelta.of(0, 0, -20, 0, 0).addTo(baseDate)).isEqualTo(LocalDate.of(2024, 12, 26));
        assertThat(DateTimeDelta.of(2, -3, 1, 0, 0).addTo(baseDate)).isEqualTo(LocalDate.of(2026, 10, 16));
        assertThatThrownBy(() -> DateTimeDelta.of(0, 0, 0, 4325, 0).addTo(baseDate)).isInstanceOf(UnsupportedTemporalTypeException.class);
        assertThatThrownBy(() -> DateTimeDelta.of(1, 2, 0, 1435, 21).addTo(baseDate)).isInstanceOf(UnsupportedTemporalTypeException.class);
    }

    @Test
    void testAddToLocalDateTime() {
        LocalDateTime baseDateTime = LocalDateTime.of(2025, 1, 15, 7, 12, 41, 324_500_000);
        assertThat(DateTimeDelta.ZERO.addTo(baseDateTime)).isEqualTo(baseDateTime);
        assertThat(DateTimeDelta.of(0, 0, 0, 1, 0).addTo(baseDateTime)).isEqualTo(LocalDateTime.of(2025, 1, 15, 7, 12, 42, 324_500_000));
        assertThat(DateTimeDelta.of(0, 0, 0, 46273, 123_456_789).addTo(baseDateTime)).isEqualTo(LocalDateTime.of(2025, 1, 15, 20, 3, 54, 447_956_789));
        assertThat(DateTimeDelta.of(0, 0, 0, 73029, 0).addTo(baseDateTime)).isEqualTo(LocalDateTime.of(2025, 1, 16, 3, 29, 50, 324_500_000));
        assertThat(DateTimeDelta.of(0, 0, 0, -425, 0).addTo(baseDateTime)).isEqualTo(LocalDateTime.of(2025, 1, 15, 7, 5, 36, 324_500_000));
        assertThat(DateTimeDelta.of(1, 0, 0, 0, 0).addTo(baseDateTime)).isEqualTo(LocalDateTime.of(2026, 1, 15, 7, 12, 41, 324_500_000));
        assertThat(DateTimeDelta.of(0, 0, 20, 0, 0).addTo(baseDateTime)).isEqualTo(LocalDateTime.of(2025, 2, 4, 7, 12, 41, 324_500_000));
        assertThat(DateTimeDelta.of(0, 0, -20, 0, 0).addTo(baseDateTime)).isEqualTo(LocalDateTime.of(2024, 12, 26, 7, 12, 41, 324_500_000));
        assertThat(DateTimeDelta.of(2, -3, 1, 0, 0).addTo(baseDateTime)).isEqualTo(LocalDateTime.of(2026, 10, 16, 7, 12, 41, 324_500_000));
        assertThat(DateTimeDelta.of(2, -3, 1, 46273, 123_456_789).addTo(baseDateTime)).isEqualTo(LocalDateTime.of(2026, 10, 16, 20, 3, 54, 447_956_789));
    }

    @Test
    void testAddToOffsetDateTime() {
        ZoneOffset offset = ZoneOffset.ofHours(2);
        OffsetDateTime baseDateTime = OffsetDateTime.of(2025, 1, 15, 7, 12, 41, 324_500_000, offset);
        assertThat(DateTimeDelta.ZERO.addTo(baseDateTime)).isEqualTo(baseDateTime);
        assertThat(DateTimeDelta.of(0, 0, 0, 1, 0).addTo(baseDateTime)).isEqualTo(OffsetDateTime.of(2025, 1, 15, 7, 12, 42, 324_500_000, offset));
        assertThat(DateTimeDelta.of(0, 0, 0, 46273, 123_456_789).addTo(baseDateTime)).isEqualTo(OffsetDateTime.of(2025, 1, 15, 20, 3, 54, 447_956_789, offset));
        assertThat(DateTimeDelta.of(0, 0, 0, 73029, 0).addTo(baseDateTime)).isEqualTo(OffsetDateTime.of(2025, 1, 16, 3, 29, 50, 324_500_000, offset));
        assertThat(DateTimeDelta.of(0, 0, 0, -425, 0).addTo(baseDateTime)).isEqualTo(OffsetDateTime.of(2025, 1, 15, 7, 5, 36, 324_500_000, offset));
        assertThat(DateTimeDelta.of(1, 0, 0, 0, 0).addTo(baseDateTime)).isEqualTo(OffsetDateTime.of(2026, 1, 15, 7, 12, 41, 324_500_000, offset));
        assertThat(DateTimeDelta.of(0, 0, 20, 0, 0).addTo(baseDateTime)).isEqualTo(OffsetDateTime.of(2025, 2, 4, 7, 12, 41, 324_500_000, offset));
        assertThat(DateTimeDelta.of(0, 0, -20, 0, 0).addTo(baseDateTime)).isEqualTo(OffsetDateTime.of(2024, 12, 26, 7, 12, 41, 324_500_000, offset));
        assertThat(DateTimeDelta.of(2, -3, 1, 0, 0).addTo(baseDateTime)).isEqualTo(OffsetDateTime.of(2026, 10, 16, 7, 12, 41, 324_500_000, offset));
        assertThat(DateTimeDelta.of(2, -3, 1, 46273, 123_456_789).addTo(baseDateTime)).isEqualTo(OffsetDateTime.of(2026, 10, 16, 20, 3, 54, 447_956_789, offset));
    }

    @Test
    void testAddToZonedDateTime() {
        ZoneId zone = ZoneId.of("Europe/Budapest");
        ZonedDateTime baseDateTime = ZonedDateTime.of(2025, 10, 25, 12, 1, 2, 3, zone);
        assertThat(DateTimeDelta.ZERO.addTo(baseDateTime)).isEqualTo(baseDateTime);
        assertThat(DateTimeDelta.of(0, 0, 0, 86400, 0).addTo(baseDateTime)).isEqualTo(ZonedDateTime.of(2025, 10, 26, 11, 1, 2, 3, zone));
        assertThat(DateTimeDelta.of(0, 0, 1, 0, 0).addTo(baseDateTime)).isEqualTo(ZonedDateTime.of(2025, 10, 26, 12, 1, 2, 3, zone));
        assertThat(DateTimeDelta.of(1, 1, 1, 1, 1).addTo(baseDateTime)).isEqualTo(ZonedDateTime.of(2026, 11, 26, 12, 1, 3, 4, zone));
        assertThat(DateTimeDelta.of(-1, -1, -1, -1, -1).addTo(baseDateTime)).isEqualTo(ZonedDateTime.of(2024, 9, 24, 12, 1, 1, 2, zone));
        assertThat(DateTimeDelta.of(-5, -5, -5, -5, -5).addTo(baseDateTime)).isEqualTo(ZonedDateTime.of(2020, 5, 20, 12, 0, 56, 999_999_998, zone));
    }

    @Test
    void testAddToInstant() {
        Instant baseInstant = Instant.ofEpochSecond(1735697561, 1234);
        assertThat(DateTimeDelta.ZERO.addTo(baseInstant)).isEqualTo(baseInstant);
        assertThat(DateTimeDelta.of(0, 0, 0, 3, 0).addTo(baseInstant)).isEqualTo(Instant.ofEpochSecond(1735697564, 1234));
        assertThat(DateTimeDelta.of(0, 0, 3, 31, 23).addTo(baseInstant)).isEqualTo(Instant.ofEpochSecond(1735956792, 1257));
        assertThat(DateTimeDelta.of(0, 0, -1, 7, 0).addTo(baseInstant)).isEqualTo(Instant.ofEpochSecond(1735611168, 1234));
        assertThat(DateTimeDelta.of(1, 1, 0, 0, 0).addTo(baseInstant)).isEqualTo(Instant.ofEpochSecond(1769911961, 1234));
        assertThat(DateTimeDelta.of(-1, -1, 0, 0, 0).addTo(baseInstant)).isEqualTo(Instant.ofEpochSecond(1701396761, 1234));
        assertThat(DateTimeDelta.of(3, -2, 1, 35123, 532_421_000).addTo(baseInstant)).isEqualTo(Instant.ofEpochSecond(1825156684, 532_422_234));
        assertThat(DateTimeDelta.of(-3, 2, -1, -35123, -532_421_000).addTo(baseInstant)).isEqualTo(Instant.ofEpochSecond(1645979237, 467_580_234));
    }

    @Test
    void testSubtractFromLocalTime() {
        LocalTime baseTime = LocalTime.of(7, 12, 41, 324_500_000);
        assertThat(DateTimeDelta.ZERO.subtractFrom(baseTime)).isEqualTo(baseTime);
        assertThat(DateTimeDelta.of(0, 0, 0, 1, 0).subtractFrom(baseTime)).isEqualTo(LocalTime.of(7, 12, 40, 324_500_000));
        assertThat(DateTimeDelta.of(0, 0, 0, 46273, 123_456_789).subtractFrom(baseTime)).isEqualTo(LocalTime.of(18, 21, 28, 201_043_211));
        assertThat(DateTimeDelta.of(0, 0, 0, 73029, 0).subtractFrom(baseTime)).isEqualTo(LocalTime.of(10, 55, 32, 324_500_000));
        assertThat(DateTimeDelta.of(0, 0, 0, -425, 0).subtractFrom(baseTime)).isEqualTo(LocalTime.of(7, 19, 46, 324_500_000));
        assertThatThrownBy(() -> DateTimeDelta.of(1, 0, 0, 0, 0).subtractFrom(baseTime)).isInstanceOf(UnsupportedTemporalTypeException.class);
        assertThatThrownBy(() -> DateTimeDelta.of(0, 1, 2, 24, 12_000_000).subtractFrom(baseTime)).isInstanceOf(UnsupportedTemporalTypeException.class);
    }

    @Test
    void testSubtractFromLocalDate() {
        LocalDate baseDate = LocalDate.of(2025, 1, 15);
        assertThat(DateTimeDelta.ZERO.subtractFrom(baseDate)).isEqualTo(baseDate);
        assertThat(DateTimeDelta.of(1, 0, 0, 0, 0).subtractFrom(baseDate)).isEqualTo(LocalDate.of(2024, 1, 15));
        assertThat(DateTimeDelta.of(0, 0, 20, 0, 0).subtractFrom(baseDate)).isEqualTo(LocalDate.of(2024, 12, 26));
        assertThat(DateTimeDelta.of(0, 0, -20, 0, 0).subtractFrom(baseDate)).isEqualTo(LocalDate.of(2025, 2, 4));
        assertThat(DateTimeDelta.of(2, -3, 1, 0, 0).subtractFrom(baseDate)).isEqualTo(LocalDate.of(2023, 4, 14));
        assertThatThrownBy(() -> DateTimeDelta.of(0, 0, 0, 4325, 0).subtractFrom(baseDate)).isInstanceOf(UnsupportedTemporalTypeException.class);
        assertThatThrownBy(() -> DateTimeDelta.of(1, 2, 0, 1435, 21).subtractFrom(baseDate)).isInstanceOf(UnsupportedTemporalTypeException.class);
    }

    @Test
    void testSubtractFromLocalDateTime() {
        LocalDateTime baseDateTime = LocalDateTime.of(2025, 1, 15, 7, 12, 41, 324_500_000);
        assertThat(DateTimeDelta.ZERO.subtractFrom(baseDateTime)).isEqualTo(baseDateTime);
        assertThat(DateTimeDelta.of(0, 0, 0, 1, 0).subtractFrom(baseDateTime)).isEqualTo(LocalDateTime.of(2025, 1, 15, 7, 12, 40, 324_500_000));
        assertThat(DateTimeDelta.of(0, 0, 0, 46273, 123_456_789).subtractFrom(baseDateTime)).isEqualTo(LocalDateTime.of(2025, 1, 14, 18, 21, 28, 201_043_211));
        assertThat(DateTimeDelta.of(0, 0, 0, 73029, 0).subtractFrom(baseDateTime)).isEqualTo(LocalDateTime.of(2025, 1, 14, 10, 55, 32, 324_500_000));
        assertThat(DateTimeDelta.of(0, 0, 0, -425, 0).subtractFrom(baseDateTime)).isEqualTo(LocalDateTime.of(2025, 1, 15, 7, 19, 46, 324_500_000));
        assertThat(DateTimeDelta.of(1, 0, 0, 0, 0).subtractFrom(baseDateTime)).isEqualTo(LocalDateTime.of(2024, 1, 15, 7, 12, 41, 324_500_000));
        assertThat(DateTimeDelta.of(0, 0, 20, 0, 0).subtractFrom(baseDateTime)).isEqualTo(LocalDateTime.of(2024, 12, 26, 7, 12, 41, 324_500_000));
        assertThat(DateTimeDelta.of(0, 0, -20, 0, 0).subtractFrom(baseDateTime)).isEqualTo(LocalDateTime.of(2025, 2, 4, 7, 12, 41, 324_500_000));
        assertThat(DateTimeDelta.of(2, -3, 1, 0, 0).subtractFrom(baseDateTime)).isEqualTo(LocalDateTime.of(2023, 4, 14, 7, 12, 41, 324_500_000));
        assertThat(DateTimeDelta.of(2, -3, 1, 46273, 123_456_789).subtractFrom(baseDateTime)).isEqualTo(LocalDateTime.of(2023, 4, 13, 18, 21, 28, 201_043_211));
    }

    @Test
    void testSubtractFromOffsetDateTime() {
        ZoneOffset offset = ZoneOffset.ofHours(2);
        OffsetDateTime baseDateTime = OffsetDateTime.of(2025, 1, 15, 7, 12, 41, 324_500_000, offset);
        assertThat(DateTimeDelta.ZERO.subtractFrom(baseDateTime)).isEqualTo(baseDateTime);
        assertThat(DateTimeDelta.of(0, 0, 0, 1, 0).subtractFrom(baseDateTime)).isEqualTo(OffsetDateTime.of(2025, 1, 15, 7, 12, 40, 324_500_000, offset));
        assertThat(DateTimeDelta.of(0, 0, 0, 46273, 123_456_789).subtractFrom(baseDateTime)).isEqualTo(OffsetDateTime.of(2025, 1, 14, 18, 21, 28, 201_043_211, offset));
        assertThat(DateTimeDelta.of(0, 0, 0, 73029, 0).subtractFrom(baseDateTime)).isEqualTo(OffsetDateTime.of(2025, 1, 14, 10, 55, 32, 324_500_000, offset));
        assertThat(DateTimeDelta.of(0, 0, 0, -425, 0).subtractFrom(baseDateTime)).isEqualTo(OffsetDateTime.of(2025, 1, 15, 7, 19, 46, 324_500_000, offset));
        assertThat(DateTimeDelta.of(1, 0, 0, 0, 0).subtractFrom(baseDateTime)).isEqualTo(OffsetDateTime.of(2024, 1, 15, 7, 12, 41, 324_500_000, offset));
        assertThat(DateTimeDelta.of(0, 0, 20, 0, 0).subtractFrom(baseDateTime)).isEqualTo(OffsetDateTime.of(2024, 12, 26, 7, 12, 41, 324_500_000, offset));
        assertThat(DateTimeDelta.of(0, 0, -20, 0, 0).subtractFrom(baseDateTime)).isEqualTo(OffsetDateTime.of(2025, 2, 4, 7, 12, 41, 324_500_000, offset));
        assertThat(DateTimeDelta.of(2, -3, 1, 0, 0).subtractFrom(baseDateTime)).isEqualTo(OffsetDateTime.of(2023, 4, 14, 7, 12, 41, 324_500_000, offset));
        assertThat(DateTimeDelta.of(2, -3, 1, 46273, 123_456_789).subtractFrom(baseDateTime)).isEqualTo(OffsetDateTime.of(2023, 4, 13, 18, 21, 28, 201_043_211, offset));
    }

    @Test
    void testSubtractFromZonedDateTime() {
        ZoneId zone = ZoneId.of("Europe/Budapest");
        ZonedDateTime baseDateTime = ZonedDateTime.of(2025, 10, 26, 11, 1, 2, 3, zone);
        assertThat(DateTimeDelta.ZERO.subtractFrom(baseDateTime)).isEqualTo(baseDateTime);
        assertThat(DateTimeDelta.of(0, 0, 0, 86400, 0).subtractFrom(baseDateTime)).isEqualTo(ZonedDateTime.of(2025, 10, 25, 12, 1, 2, 3, zone));
        assertThat(DateTimeDelta.of(0, 0, 1, 0, 0).subtractFrom(baseDateTime)).isEqualTo(ZonedDateTime.of(2025, 10, 25, 11, 1, 2, 3, zone));
        assertThat(DateTimeDelta.of(1, 1, 1, 1, 1).subtractFrom(baseDateTime)).isEqualTo(ZonedDateTime.of(2024, 9, 25, 11, 1, 1, 2, zone));
        assertThat(DateTimeDelta.of(-1, -1, -1, -1, -1).subtractFrom(baseDateTime)).isEqualTo(ZonedDateTime.of(2026, 11, 27, 11, 1, 3, 4, zone));
        assertThat(DateTimeDelta.of(-5, -5, -5, -5, -5).subtractFrom(baseDateTime)).isEqualTo(ZonedDateTime.of(2031, 3, 31, 11, 1, 7, 8, zone));
    }

    @Test
    void testSubtractFromInstant() {
        Instant baseInstant = Instant.ofEpochSecond(1735697561, 1234);
        assertThat(DateTimeDelta.ZERO.subtractFrom(baseInstant)).isEqualTo(baseInstant);
        assertThat(DateTimeDelta.of(0, 0, 0, 3, 0).subtractFrom(baseInstant)).isEqualTo(Instant.ofEpochSecond(1735697558, 1234));
        assertThat(DateTimeDelta.of(0, 0, 3, 31, 23).subtractFrom(baseInstant)).isEqualTo(Instant.ofEpochSecond(1735438330, 1211));
        assertThat(DateTimeDelta.of(0, 0, -1, 7, 0).subtractFrom(baseInstant)).isEqualTo(Instant.ofEpochSecond(1735783954, 1234));
        assertThat(DateTimeDelta.of(1, 1, 0, 0, 0).subtractFrom(baseInstant)).isEqualTo(Instant.ofEpochSecond(1701396761, 1234));
        assertThat(DateTimeDelta.of(-1, -1, 0, 0, 0).subtractFrom(baseInstant)).isEqualTo(Instant.ofEpochSecond(1769911961, 1234));
        assertThat(DateTimeDelta.of(3, -2, 1, 35123, 532_421_000).subtractFrom(baseInstant)).isEqualTo(Instant.ofEpochSecond(1645979237, 467_580_234));
        assertThat(DateTimeDelta.of(-3, 2, -1, -35123, -532_421_000).subtractFrom(baseInstant)).isEqualTo(Instant.ofEpochSecond(1825156684, 532_422_234));
    }

    @Test
    void testCompareTo() {
        assertThat(DateTimeDelta.of(1, 2, 3, 4, 5)).isEqualByComparingTo(DateTimeDelta.of(1, 2, 3, 4, 5));
        assertThat(DateTimeDelta.of(85, -92, 0, 7, -1)).isEqualByComparingTo(DateTimeDelta.of(85, -92, 0, 7, -1));
        assertThat(DateTimeDelta.of(12, 24, 33, 1, 2)).isGreaterThan(DateTimeDelta.of(10, 22, 33, 1, 0));
        assertThat(DateTimeDelta.of(2, 2, 0, 0, 0)).isGreaterThan(DateTimeDelta.of(1, 55, 0, 0, 0));
        assertThat(DateTimeDelta.of(11, 20, 2, 6, 9)).isLessThan(DateTimeDelta.of(11, 24, 2, 7, 9));
        assertThat(DateTimeDelta.of(11, 20, 2, 6, 9)).isLessThan(DateTimeDelta.of(12, 0, 2, 5, 9));
    }

    @Test
    void testHashCode() {
        assertThat(DateTimeDelta.of(1, 2, 3, 4, 5)).hasSameHashCodeAs(DateTimeDelta.of(1, 2, 3, 4, 5));
        assertThat(DateTimeDelta.of(85, -92, 0, 7, -1)).hasSameHashCodeAs(DateTimeDelta.of(85, -92, 0, 7, -1));
    }

    @Test
    void testEquals() {
        assertThat(DateTimeDelta.of(10, 9, 8, 7, 6)).isEqualTo(DateTimeDelta.of(10, 9, 8, 7, 6));
        assertThat(DateTimeDelta.of(-1, 5, -2, 7, 1)).isEqualTo(DateTimeDelta.of(-1, 5, -2, 7, 1));
        assertThat(DateTimeDelta.of(-1, 5, -2, 7)).isEqualTo(DateTimeDelta.of(-1, 5, -2, 7, 0));
    }

    @Test
    void testNotEquals() {
        assertThat(DateTimeDelta.of(1, 2, 3, 4, 5)).isNotEqualTo(DateTimeDelta.of(10, 9, 8, 7, 6));
        assertThat(DateTimeDelta.of(1, 0, 0, 0, 0)).isNotEqualTo(DateTimeDelta.of(0, 12, 0, 0, 0));
        assertThat(DateTimeDelta.of(0, 0, 0, 86400, 0)).isNotEqualTo(DateTimeDelta.of(0, 0, 1, 0, 0));
    }

    @Test
    void testToString() {
        assertThat(DateTimeDelta.ZERO).hasToString("PT0S");
        assertThat(DateTimeDelta.of(Period.ZERO, Duration.ofSeconds(425_251))).hasToString("PT118H7M31S");
        assertThat(DateTimeDelta.of(Period.ZERO, Duration.ofSeconds(-4121))).hasToString("PT-1H-8M-41S");
        assertThat(DateTimeDelta.of(Period.ZERO, Duration.ofSeconds(134_634, 483_253_423))).hasToString("PT37H23M54.483253423S");
        assertThat(DateTimeDelta.of(Period.ZERO, Duration.ofDays(2))).hasToString("PT48H");
        assertThat(DateTimeDelta.of(Period.ZERO, Duration.ofHours(18))).hasToString("PT18H");
        assertThat(DateTimeDelta.of(Period.ZERO, Duration.ofMinutes(55))).hasToString("PT55M");
        assertThat(DateTimeDelta.of(Period.ZERO, Duration.ofSeconds(13))).hasToString("PT13S");
        assertThat(DateTimeDelta.of(Period.ZERO, Duration.ofSeconds(45, 3124))).hasToString("PT45.000003124S");
        assertThat(DateTimeDelta.of(Period.of(3, 42, 5), Duration.ZERO)).hasToString("P3Y42M5D");
        assertThat(DateTimeDelta.of(Period.ofYears(12), Duration.ZERO)).hasToString("P12Y");
        assertThat(DateTimeDelta.of(Period.ofMonths(25), Duration.ZERO)).hasToString("P25M");
        assertThat(DateTimeDelta.of(Period.ofDays(41), Duration.ZERO)).hasToString("P41D");
        assertThat(DateTimeDelta.of(Period.of(-3, 5, -1), Duration.ZERO)).hasToString("P-3Y5M-1D");
        assertThat(DateTimeDelta.of(Period.of(5, 29, 43), Duration.ofSeconds(12_345))).hasToString("P5Y29M43DT3H25M45S");
        assertThat(DateTimeDelta.of(Period.of(5, 29, 41), Duration.ofSeconds(-12_345))).hasToString("P5Y29M41DT-3H-25M-45S");
        assertThat(DateTimeDelta.of(Period.of(5, 29, -41), Duration.ofSeconds(2))).hasToString("P5Y29M-41DT2S");
    }

    @Test
    void testIsZero() {
        assertThat(DateTimeDelta.ZERO.isZero()).isTrue();
        assertThat(DateTimeDelta.of(0, 0, 0, 0, 1).isZero()).isFalse();
        assertThat(DateTimeDelta.of(0, 0, 1, 0, 0).isZero()).isFalse();
        assertThat(DateTimeDelta.of(0, 0, 1, -1, 0).isZero()).isFalse();
    }

    @Test
    void testIsNegative() {
        assertThat(DateTimeDelta.ZERO.isNegative()).isFalse();
        assertThat(DateTimeDelta.of(0, 0, 0, 0, 1).isNegative()).isFalse();
        assertThat(DateTimeDelta.of(0, 0, 1, 0, 0).isNegative()).isFalse();
        assertThat(DateTimeDelta.of(0, 0, 1, 1, 0).isNegative()).isFalse();
        assertThat(DateTimeDelta.of(0, 0, 1, -1, 0).isNegative()).isTrue();
        assertThat(DateTimeDelta.of(0, 0, -1, 1, 0).isNegative()).isTrue();
        assertThat(DateTimeDelta.of(2025, 11, 1, 1, 1).isNegative()).isFalse();
        assertThat(DateTimeDelta.of(2025, -11, 1, 1, 1).isNegative()).isTrue();
    }

    @Test
    void testNegated() {
        assertThat(DateTimeDelta.ZERO.negated()).isEqualTo(DateTimeDelta.ZERO);
        assertThat(DateTimeDelta.of(0, 0, 0, 0, 1).negated()).isEqualTo(DateTimeDelta.of(0, 0, 0, 0, -1));
        assertThat(DateTimeDelta.of(0, 0, 1, 0, 0).negated()).isEqualTo(DateTimeDelta.of(0, 0, -1, 0, 0));
        assertThat(DateTimeDelta.of(2025, -11, 1, -23, -312_324_235).negated()).isEqualTo(DateTimeDelta.of(-2025, 11, -1, 23, 312_324_235));
    }

    @Test
    void testPlus() {
        DateTimeDelta delta = DateTimeDelta.of(12, 4, 1, 1000, 111_111_111);
        assertThat(delta.plus(DateTimeDelta.ZERO)).isEqualTo(delta);
        assertThat(delta.plus(Duration.ZERO)).isEqualTo(delta);
        assertThat(delta.plus(Period.ZERO)).isEqualTo(delta);
        assertThat(delta.plus(Duration.ofSeconds(423, 12301))).isEqualTo(DateTimeDelta.of(12, 4, 1, 1423, 111_123_412));
        assertThat(delta.plus(Period.of(7, -12, -1))).isEqualTo(DateTimeDelta.of(19, -8, 0, 1000, 111_111_111));
        assertThat(delta.plus(DateTimeDelta.of(3, -2, -5, 600, -2_034))).isEqualTo(DateTimeDelta.of(15, 2, -4, 1600, 111_109_077));
    }

    @Test
    void testPlusUnit() {
        DateTimeDelta delta = DateTimeDelta.of(12, 4, 1, 1000, 111_111_111);
        assertThat(delta.plus(0, ChronoUnit.SECONDS)).isEqualTo(delta);
        assertThat(delta.plus(3, ChronoUnit.NANOS)).isEqualTo(DateTimeDelta.of(12, 4, 1, 1000, 111_111_114));
        assertThat(delta.plus(-4, ChronoUnit.NANOS)).isEqualTo(DateTimeDelta.of(12, 4, 1, 1000, 111_111_107));
        assertThat(delta.plus(412, ChronoUnit.MICROS)).isEqualTo(DateTimeDelta.of(12, 4, 1, 1000, 111_523_111));
        assertThat(delta.plus(-72, ChronoUnit.MICROS)).isEqualTo(DateTimeDelta.of(12, 4, 1, 1000, 111_039_111));
        assertThat(delta.plus(40, ChronoUnit.MILLIS)).isEqualTo(DateTimeDelta.of(12, 4, 1, 1000, 151_111_111));
        assertThat(delta.plus(-3105, ChronoUnit.MILLIS)).isEqualTo(DateTimeDelta.of(12, 4, 1, 997, 6_111_111));
        assertThat(delta.plus(1500, ChronoUnit.SECONDS)).isEqualTo(DateTimeDelta.of(12, 4, 1, 2500, 111_111_111));
        assertThat(delta.plus(-1400, ChronoUnit.SECONDS)).isEqualTo(DateTimeDelta.of(12, 4, 1, -399, -888_888_889));
        assertThat(delta.plus(2, ChronoUnit.MINUTES)).isEqualTo(DateTimeDelta.of(12, 4, 1, 1120, 111_111_111));
        assertThat(delta.plus(-1, ChronoUnit.MINUTES)).isEqualTo(DateTimeDelta.of(12, 4, 1, 940, 111_111_111));
        assertThat(delta.plus(3, ChronoUnit.HOURS)).isEqualTo(DateTimeDelta.of(12, 4, 1, 11800, 111_111_111));
        assertThat(delta.plus(-2, ChronoUnit.HOURS)).isEqualTo(DateTimeDelta.of(12, 4, 1, -6199, -888_888_889));
        assertThat(delta.plus(1, ChronoUnit.HALF_DAYS)).isEqualTo(DateTimeDelta.of(12, 4, 1, 44200, 111_111_111));
        assertThat(delta.plus(-1, ChronoUnit.HALF_DAYS)).isEqualTo(DateTimeDelta.of(12, 4, 1, -42199, -888_888_889));
        assertThat(delta.plus(5, ChronoUnit.DAYS)).isEqualTo(DateTimeDelta.of(12, 4, 6, 1000, 111_111_111));
        assertThat(delta.plus(-6, ChronoUnit.DAYS)).isEqualTo(DateTimeDelta.of(12, 4, -5, 1000, 111_111_111));
        assertThat(delta.plus(2, ChronoUnit.WEEKS)).isEqualTo(DateTimeDelta.of(12, 4, 15, 1000, 111_111_111));
        assertThat(delta.plus(-2, ChronoUnit.WEEKS)).isEqualTo(DateTimeDelta.of(12, 4, -13, 1000, 111_111_111));
        assertThat(delta.plus(5, ChronoUnit.MONTHS)).isEqualTo(DateTimeDelta.of(12, 9, 1, 1000, 111_111_111));
        assertThat(delta.plus(-7, ChronoUnit.MONTHS)).isEqualTo(DateTimeDelta.of(12, -3, 1, 1000, 111_111_111));
        assertThat(delta.plus(11, ChronoUnit.YEARS)).isEqualTo(DateTimeDelta.of(23, 4, 1, 1000, 111_111_111));
        assertThat(delta.plus(-12, ChronoUnit.YEARS)).isEqualTo(DateTimeDelta.of(0, 4, 1, 1000, 111_111_111));
        assertThat(delta.plus(2, ChronoUnit.DECADES)).isEqualTo(DateTimeDelta.of(32, 4, 1, 1000, 111_111_111));
        assertThat(delta.plus(-3, ChronoUnit.DECADES)).isEqualTo(DateTimeDelta.of(-18, 4, 1, 1000, 111_111_111));
        assertThat(delta.plus(1, ChronoUnit.CENTURIES)).isEqualTo(DateTimeDelta.of(112, 4, 1, 1000, 111_111_111));
        assertThat(delta.plus(-1, ChronoUnit.CENTURIES)).isEqualTo(DateTimeDelta.of(-88, 4, 1, 1000, 111_111_111));
        assertThat(delta.plus(7, ChronoUnit.MILLENNIA)).isEqualTo(DateTimeDelta.of(7012, 4, 1, 1000, 111_111_111));
        assertThat(delta.plus(-7, ChronoUnit.MILLENNIA)).isEqualTo(DateTimeDelta.of(-6988, 4, 1, 1000, 111_111_111));
        assertThat(delta.plus(2, ChronoUnit.ERAS)).isEqualTo(DateTimeDelta.of(2_000_000_012, 4, 1, 1000, 111_111_111));
        assertThat(delta.plus(-2, ChronoUnit.ERAS)).isEqualTo(DateTimeDelta.of(-1_999_999_988, 4, 1, 1000, 111_111_111));
        assertThat(delta.plus(0, ChronoUnit.FOREVER)).isEqualTo(delta);
        assertThat(delta.plus(1, ChronoUnit.FOREVER)).isEqualTo(DateTimeDelta.MAX_VALUE);
        assertThat(delta.plus(5, ChronoUnit.FOREVER)).isEqualTo(DateTimeDelta.MAX_VALUE);
        assertThat(delta.plus(-1, ChronoUnit.FOREVER)).isEqualTo(DateTimeDelta.MIN_VALUE);
        assertThat(delta.plus(-4, ChronoUnit.FOREVER)).isEqualTo(DateTimeDelta.MIN_VALUE);
    }

    @Test
    void testPlusUnitOverflow() {
        DateTimeDelta delta = DateTimeDelta.of(12, 4, 1, 1000, 111_111_111);
        assertThatThrownBy(() -> delta.plus(100, ChronoUnit.ERAS)).isInstanceOf(ArithmeticException.class);
        assertThatThrownBy(() -> delta.plus(-100, ChronoUnit.ERAS)).isInstanceOf(ArithmeticException.class);
    }

    @Test
    void testMinus() {
        DateTimeDelta delta = DateTimeDelta.of(12, 4, 1, 1000, 111_111_111);
        assertThat(delta.minus(DateTimeDelta.ZERO)).isEqualTo(delta);
        assertThat(delta.minus(Duration.ZERO)).isEqualTo(delta);
        assertThat(delta.minus(Period.ZERO)).isEqualTo(delta);
        assertThat(delta.minus(Duration.ofSeconds(423, 12301))).isEqualTo(DateTimeDelta.of(12, 4, 1, 577, 111_098_810));
        assertThat(delta.minus(Period.of(7, -12, -1))).isEqualTo(DateTimeDelta.of(5, 16, 2, 1000, 111_111_111));
        assertThat(delta.minus(DateTimeDelta.of(3, -2, -5, 600, -2_034))).isEqualTo(DateTimeDelta.of(9, 6, 6, 400, 111_113_145));
    }

    @Test
    void testMinusUnit() {
        DateTimeDelta delta = DateTimeDelta.of(12, 4, 1, 1000, 111_111_111);
        assertThat(delta.minus(0, ChronoUnit.SECONDS)).isEqualTo(delta);
        assertThat(delta.minus(3, ChronoUnit.NANOS)).isEqualTo(DateTimeDelta.of(12, 4, 1, 1000, 111_111_108));
        assertThat(delta.minus(-4, ChronoUnit.NANOS)).isEqualTo(DateTimeDelta.of(12, 4, 1, 1000, 111_111_115));
        assertThat(delta.minus(412, ChronoUnit.MICROS)).isEqualTo(DateTimeDelta.of(12, 4, 1, 1000, 110_699_111));
        assertThat(delta.minus(-72, ChronoUnit.MICROS)).isEqualTo(DateTimeDelta.of(12, 4, 1, 1000, 111_183_111));
        assertThat(delta.minus(40, ChronoUnit.MILLIS)).isEqualTo(DateTimeDelta.of(12, 4, 1, 1000, 71_111_111));
        assertThat(delta.minus(-3105, ChronoUnit.MILLIS)).isEqualTo(DateTimeDelta.of(12, 4, 1, 1003, 216_111_111));
        assertThat(delta.minus(1500, ChronoUnit.SECONDS)).isEqualTo(DateTimeDelta.of(12, 4, 1, -499, -888_888_889));
        assertThat(delta.minus(-1400, ChronoUnit.SECONDS)).isEqualTo(DateTimeDelta.of(12, 4, 1, 2400, 111_111_111));
        assertThat(delta.minus(2, ChronoUnit.MINUTES)).isEqualTo(DateTimeDelta.of(12, 4, 1, 880, 111_111_111));
        assertThat(delta.minus(-1, ChronoUnit.MINUTES)).isEqualTo(DateTimeDelta.of(12, 4, 1, 1060, 111_111_111));
        assertThat(delta.minus(3, ChronoUnit.HOURS)).isEqualTo(DateTimeDelta.of(12, 4, 1, -9799, -888_888_889));
        assertThat(delta.minus(-2, ChronoUnit.HOURS)).isEqualTo(DateTimeDelta.of(12, 4, 1, 8200, 111_111_111));
        assertThat(delta.minus(1, ChronoUnit.HALF_DAYS)).isEqualTo(DateTimeDelta.of(12, 4, 1, -42199, -888_888_889));
        assertThat(delta.minus(-1, ChronoUnit.HALF_DAYS)).isEqualTo(DateTimeDelta.of(12, 4, 1, 44200, 111_111_111));
        assertThat(delta.minus(5, ChronoUnit.DAYS)).isEqualTo(DateTimeDelta.of(12, 4, -4, 1000, 111_111_111));
        assertThat(delta.minus(-6, ChronoUnit.DAYS)).isEqualTo(DateTimeDelta.of(12, 4, 7, 1000, 111_111_111));
        assertThat(delta.minus(2, ChronoUnit.WEEKS)).isEqualTo(DateTimeDelta.of(12, 4, -13, 1000, 111_111_111));
        assertThat(delta.minus(-2, ChronoUnit.WEEKS)).isEqualTo(DateTimeDelta.of(12, 4, 15, 1000, 111_111_111));
        assertThat(delta.minus(5, ChronoUnit.MONTHS)).isEqualTo(DateTimeDelta.of(12, -1, 1, 1000, 111_111_111));
        assertThat(delta.minus(-7, ChronoUnit.MONTHS)).isEqualTo(DateTimeDelta.of(12, 11, 1, 1000, 111_111_111));
        assertThat(delta.minus(11, ChronoUnit.YEARS)).isEqualTo(DateTimeDelta.of(1, 4, 1, 1000, 111_111_111));
        assertThat(delta.minus(-12, ChronoUnit.YEARS)).isEqualTo(DateTimeDelta.of(24, 4, 1, 1000, 111_111_111));
        assertThat(delta.minus(2, ChronoUnit.DECADES)).isEqualTo(DateTimeDelta.of(-8, 4, 1, 1000, 111_111_111));
        assertThat(delta.minus(-3, ChronoUnit.DECADES)).isEqualTo(DateTimeDelta.of(42, 4, 1, 1000, 111_111_111));
        assertThat(delta.minus(1, ChronoUnit.CENTURIES)).isEqualTo(DateTimeDelta.of(-88, 4, 1, 1000, 111_111_111));
        assertThat(delta.minus(-1, ChronoUnit.CENTURIES)).isEqualTo(DateTimeDelta.of(112, 4, 1, 1000, 111_111_111));
        assertThat(delta.minus(7, ChronoUnit.MILLENNIA)).isEqualTo(DateTimeDelta.of(-6988, 4, 1, 1000, 111_111_111));
        assertThat(delta.minus(-7, ChronoUnit.MILLENNIA)).isEqualTo(DateTimeDelta.of(7012, 4, 1, 1000, 111_111_111));
        assertThat(delta.minus(2, ChronoUnit.ERAS)).isEqualTo(DateTimeDelta.of(-1_999_999_988, 4, 1, 1000, 111_111_111));
        assertThat(delta.minus(-2, ChronoUnit.ERAS)).isEqualTo(DateTimeDelta.of(2_000_000_012, 4, 1, 1000, 111_111_111));
        assertThat(delta.minus(0, ChronoUnit.FOREVER)).isEqualTo(delta);
        assertThat(delta.minus(1, ChronoUnit.FOREVER)).isEqualTo(DateTimeDelta.MIN_VALUE);
        assertThat(delta.minus(5, ChronoUnit.FOREVER)).isEqualTo(DateTimeDelta.MIN_VALUE);
        assertThat(delta.minus(-1, ChronoUnit.FOREVER)).isEqualTo(DateTimeDelta.MAX_VALUE);
        assertThat(delta.minus(-4, ChronoUnit.FOREVER)).isEqualTo(DateTimeDelta.MAX_VALUE);
    }

    @Test
    void testMinusUnitOverflow() {
        DateTimeDelta delta = DateTimeDelta.of(12, 4, 1, 1000, 111_111_111);
        assertThatThrownBy(() -> delta.minus(100, ChronoUnit.ERAS)).isInstanceOf(ArithmeticException.class);
        assertThatThrownBy(() -> delta.minus(-100, ChronoUnit.ERAS)).isInstanceOf(ArithmeticException.class);
    }

    @Test
    void testNormalized() {
        assertThat(DateTimeDelta.ZERO.normalized()).isEqualTo(DateTimeDelta.ZERO);
        assertThat(DateTimeDelta.of(0, 0, 0, 412, 735_031_233).normalized()).isEqualTo(DateTimeDelta.of(0, 0, 0, 412, 735_031_233));
        assertThat(DateTimeDelta.of(0, 0, 0, -412, -735_031_233).normalized()).isEqualTo(DateTimeDelta.of(0, 0, 0, -412, -735_031_233));
        assertThat(DateTimeDelta.of(2, 4, 17, 0, 0).normalized()).isEqualTo(DateTimeDelta.of(2, 4, 17, 0, 0));
        assertThat(DateTimeDelta.of(2, 4, -17, 0, 0).normalized()).isEqualTo(DateTimeDelta.of(2, 4, -17, 0, 0));
        assertThat(DateTimeDelta.of(12, 3, 2, 13013, 123_456_789).normalized()).isEqualTo(DateTimeDelta.of(12, 3, 2, 13013, 123_456_789));
        assertThat(DateTimeDelta.of(12, 3, 2, -129600, 0).normalized()).isEqualTo(DateTimeDelta.of(12, 3, 1, -43200, 0));
        assertThat(DateTimeDelta.of(12, -3, 2, -135, 0).normalized()).isEqualTo(DateTimeDelta.of(11, 9, 2, -135, 0));
        assertThat(DateTimeDelta.of(7, 26, 2, 129600, 253_704_601).normalized()).isEqualTo(DateTimeDelta.of(9, 2, 3, 43200, 253_704_601));
        assertThat(DateTimeDelta.of(1, -1, -1, 143521, 142).normalized()).isEqualTo(DateTimeDelta.of(0, 11, 0, 57121, 142));
    }

    @Test
    void testCollapsed() {
        assertThat(DateTimeDelta.ZERO.collapsed()).isEqualTo(DateTimeDelta.ZERO);
        assertThat(DateTimeDelta.of(0, 0, 0, 412, 735_031_233).collapsed()).isEqualTo(DateTimeDelta.of(0, 0, 0, 412, 735_031_233));
        assertThat(DateTimeDelta.of(0, 0, 0, -412, -735_031_233).collapsed()).isEqualTo(DateTimeDelta.of(0, 0, 0, -412, -735_031_233));
        assertThat(DateTimeDelta.of(2, 4, 17, 0, 0).collapsed()).isEqualTo(DateTimeDelta.of(2, 4, 17, 0, 0));
        assertThat(DateTimeDelta.of(2, -4, 17, 0, 0).collapsed()).isEqualTo(DateTimeDelta.of(1, 8, 17, 0, 0));
        assertThat(DateTimeDelta.of(-2, 4, 17, 0, 0).collapsed()).isEqualTo(DateTimeDelta.of(-1, -7, -13, 0, 0));
        assertThat(DateTimeDelta.of(1, 0, -1, 0, 0).collapsed()).isEqualTo(DateTimeDelta.of(0, 11, 29, 0, 0));
        assertThat(DateTimeDelta.of(1, 0, 0, 0, -12).collapsed()).isEqualTo(DateTimeDelta.of(0, 11, 29, 86399, 999_999_988));
        assertThat(DateTimeDelta.of(1, 1, -1, 0, -53).collapsed()).isEqualTo(DateTimeDelta.of(1, 0, 28, 86399, 999_999_947));
        assertThat(DateTimeDelta.of(31, -17, 1, -120432, -111_111_111).collapsed()).isEqualTo(DateTimeDelta.of(29, 6, 29, 52367, 888_888_889));
        assertThat(DateTimeDelta.of(-31, 17, -1, 120432, 111_111_111).collapsed()).isEqualTo(DateTimeDelta.of(-29, -6, -29, -52367, -888_888_889));
    }

    @Test
    void testToDuration() {
        assertThat(DateTimeDelta.ZERO.toDuration()).isEqualTo(Duration.ZERO);
        assertThat(DateTimeDelta.of(0, 0, 0, 412, 735_031_233).toDuration()).isEqualTo(Duration.ofSeconds(412, 735_031_233));
        assertThat(DateTimeDelta.of(0, 0, 0, -504, -123_456_789).toDuration()).isEqualTo(Duration.ofSeconds(-504, -123_456_789));
        assertThat(DateTimeDelta.of(0, 0, 1, 0, 0).toDuration()).isEqualTo(Duration.ofSeconds(86400, 0));
        assertThat(DateTimeDelta.of(0, 3, 0, 0).toDuration()).isEqualTo(Duration.ofSeconds(7889238, 0));
        assertThat(DateTimeDelta.of(-2, 0, 0, 0).toDuration()).isEqualTo(Duration.ofSeconds(-63113904, 0));
        assertThat(DateTimeDelta.of(1, 2, 3, 0, 0).toDuration()).isEqualTo(Duration.ofSeconds(37075644, 0));
        assertThat(DateTimeDelta.of(1, -2, -3, 0, 0).toDuration()).isEqualTo(Duration.ofSeconds(26038260, 0));
        assertThat(DateTimeDelta.of(0, 2, 0, 0, 10).toDuration()).isEqualTo(Duration.ofSeconds(5259492, 10));
        assertThat(DateTimeDelta.of(0, 0, 2, -3600, -12).toDuration()).isEqualTo(Duration.ofSeconds(169199, 999_999_988));
        assertThat(DateTimeDelta.of(2, 4, 17, 14672, 421_301_037).toDuration()).isEqualTo(Duration.ofSeconds(75116360, 421_301_037));
        assertThat(DateTimeDelta.of(2, -4, 17, -14672, -421_301_037).toDuration()).isEqualTo(Duration.ofSeconds(54049047, 578698963));
    }

}
