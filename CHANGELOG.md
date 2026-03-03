# Changelog


## Version 0.5.0

Released on *2026-03-03*

### Added:

- Added the `DateTimeDelta` type
- Added the `ReachabilityGuard` type

### Improved:

- Made `ByteString` comparable
- Improved JSON serialization for `ImmutableList`
- Improved API and behavior of `ImmutableList`

### Development:

- Updated to gradle 9 and improved build


## Version 0.4.0

Released on *2025-09-29*

### Improved:

- Improved JSON serialization for ImmutableList

### Development:

- Improved project tools


## Version 0.3.0

Released on *2025-03-15*

### Fixed:

- Fixed division overflow bug in LargeInteger
- Ensured Java8 compatibility in LargeInteger

### Changed:

- Renamed `nonNegativeOf()` to `ofUnsigned()` in `LargeInteger`

### Added:

- Added new useful methods to `ImmutableList`
- Added `FindPositionResult` and `ToStringBuilder`

### Improved:

- Improved performance of `LargeInteger`
- Extended LargeInteger with more features

### Development:

- Upgrade to gradle 8
- Improve and extend unit tests and benchmarks


## Version 0.2.0

Released on *2023-12-16*

### Fixed:

- Fixed and improved many methods of `LargeInteger`
- Fixed index handling in resize method of `ImmutableList`
- Fixed iterator of `ByteString`

### Improved:

- Added a large amount of tests for `lang`
- Added module definition and utf-8 encoding explicitly
- Added many other little improvements


## Version 0.1.0

Released on *2023-01-11*

### Migration:

- Project migrated from the main `miniconnect` repo

### Added:

- Added new features and improvements to `lang`
