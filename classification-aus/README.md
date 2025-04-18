# Australian Classification/Protective Marking Model

This project contains a data model for Australian Protective Markings, as defined in the
[PSPF Annual Release](./documents/pspf-release/README.md).

This model is derived solely from publicly available documents. The set of documents referenced when designing this
model may be found in [Documents](./documents/README.md). These documents are:
- [PSPF Annual Release](./documents/pspf-release/README.md)
- [PSPF Release Guidelines](./documents/pspf-release-guidelines/README.md)
- [Email Protective Marking Standard](./documents/email-protective-marking-standard/README.md)
- [Record Keeping Metadata Standard](./documents/recordkeeping-metadata-standard/README.md)

### Purpose

This model exists to deal with the issue that data stored in computer systems frequently needs to maintain the
classification of the data. This is typically done in ad hoc ways, so that every data structure and library stores it's
classification data in different formats. Which then need to be mapped from one format to another, whenever the
different data sets are merged. Also each ad hoc format tends to support only a limited part of protective marking
standard - leading to issues with converting between formats, and with the periodic need to enhance an existing
format with additional fields.

In this project the model is intended to be comprehensive, and suitable for use as Data Transfer Objects. The models
are based on `record`'s, with `unmodifiable` collections, to ensure immutability. This also makes these models safe
for use in threaded environments.

## The Two purposes of the `ProtectiveMarkerBuilder`

### Creating new ProtectiveMarking's

To create new `ProtectiveMarker`'s, first create an instance of `ProteciveMarkerBuilder`. Populate the fields of the
builder, and then use the `build()` function to create a new `ProtectiveMarker`. Use of the builder ensures that the
resulting `ProtectiveMarker` is valid.

### Handling Invalid States (particularly in UI's)

In the course of working with Classified data, it is not unusual to need to hold representations of 
`ProtectiveMarker`'s in invalid, intermediate states, while the User edits them via the UI. One of the
`ProtectiveMarkerBuilder`'s purposes is to be able to hold `ProtectiveMarker` information in an invalid state, while a
User is actively editing it via a UI.

## ProtectiveMarking structure

The `ProtectiveMarker` `record` consists of the following fields:

### Classification

The `Classification` for this `ProtectiveMarker`. The list of Classifications is derived from the
[PSPF Annual Release](./documents/pspf-release/README.md) **Section 9.2**.

The `Classification` is required.

The Classifications are:
- UNOFFICIAL
- OFFICIAL
- OFFICIAL: Sensitive
- PROTECTED
- SECRET
- TOP SECRET

##### Default Classifications

By default the library is configured to use a set of `DevelopmentClassification`'s. These are deliberately distinct
from the real set of `Classification`'s (`PSPFClassification`). Configuring `ProductionMode` to be true will cause the
library to start using the real set of `Classification`'s.

`DevelopmentClassification`'s exist to allow development and testing of Classification related logic and data storage
in less secure environments than the final target production environment.

### Information Management Markers

An optional way to identify information that is subject to non security related restrictions on access and use,
as defined in [PSPF Annual Release](./documents/pspf-release/README.md) **Section 9.4**, with the full list of
Information Management Markers supported in this library listed in
[PSPF Release Guidelines](./documents/pspf-release-guidelines/README.md) **Section 9.4** Table 23.

As specified in [Email Protective Marking Standard](./documents/email-protective-marking-standard/README.md)
**Section 7.1** Table 1, there may be any number of Information Management Markers starting from zero.

If there are no Information Management Markers in the `ProtectiveMarker` then the `informationManagementMarkers`
List will be empty.

Supported Information Management Markers are:
- Legal Privilege
- Legislative Secrecy
- Personal Privacy

### SecurityCaveats

`SecurityCaveats` encapsulates a number of other markings. If none of these markings are present, then the
`securityCaveats` field in the `ProtectiveMarker` will be null.

#### Codewords

Code words and Compartments (sensitive compartment information that requires a compartmental briefing). Defined in
[PSPF Annual Release](./documents/pspf-release/README.md) **Section 9.5.1** and
[PSPF Release Guidelines](./documents/pspf-release-guidelines/README.md) **Section 9.5.1** Table 24. Code Words
are sorted in alphabetical order.

If there are no code words, then this list will be empty.

#### Foreign Government Markings

Foreign government markings are applied to information created by Australian entities from foreign source information.
As defined in [PSPF Annual Release](./documents/pspf-release/README.md) **Section 9.5.1** and
[PSPF Release Guidelines](./documents/pspf-release-guidelines/README.md) **Section 9.5.1** Table 24.

It is not clear from the documentation if it makes sense for there to be multiple Foreign Government Markings on
a single `ProtectiveMarker`. This library is supporting multiple markings, which will be sorted in alphabetical
order.

If there are no foreign government markings, then this list will be empty.

#### Special Handling Caveat

A `SpecialHandlingCaveat` is made up of a `SpecialHandlingInstruction` and an `exclusiveFor` addressee string.

Special handling instructions indicate particular precautions for information handling. These are defined in
[PSPF Annual Release](./documents/pspf-release/README.md) **Section 9.5.1** and
[PSPF Release Guidelines](./documents/pspf-release-guidelines/README.md) **Section 9.5.1** Table 24. The full list
of Special Handling Instructions supported by this library are defined in the
[Email Protective Marking Standard](./documents/email-protective-marking-standard/README.md) **Section 7.1** Table 1.

The [Email Protective Marking Standard](./documents/email-protective-marking-standard/README.md) strongly implies that
each `ProtectiveMarker` can only have one Special Handling Instruction.

If there is no Special Handling Instruction, then the `specialHandlingCaveat` field will be null.

Special Handling Instructions supported by this library are:
- DELICATE-SOURCE
- ORCON
- EXCLUSIVE-FOR
- CABINET
- NATIONAL-CABNET

NATIONAL-CABINET is obsolete, and should not be used. The `ProtectiveMarkerBuilder` includes functions for setting
each of the above Special Handling Instructions, except NATIONAL-CABINET.

**NOTE:** It is not at all clear from the documentation that this list of Special Handling Instructions is
comprehensive. However, it does list all instructions listed in the source documentation.

#### Releasability

A `ReleasabilityCaveat` is made up of a `ReleasabilityType` and a `releasable to` list.

`Releasability` has three types, as defined in 
[PSPF Release Guidelines](./documents/pspf-release-guidelines/README.md) **Section 9.5.1** Table 24. The
releasabilities are:
- AUSTEO
- AGAO
- REL

The `releasable to` list of the `ReleasabilityCaveat` is only to be populated if the `releasable type` is REL.

When the Releasable type is REL, then the `releasable to` list should be populated with a minimum of 2 values, one
of which must be `AUS`. `Releasable to` lists are ordered with the Five Eyes countries first, and then in alphabetical
oder.

`Releasable to` lists are expected to be populated with country trigraphs, as defined in the 
`International Standard ISO 3166-1:2013 alpha3 Codes`. This is not enforced, except for requiring that `AUS` be present.

If there is no Releasability, then the `releasabilityCaveat` field will be null.

## Configuration

##### Releasable To Oder

Determines which `Comparator` to use for sorting Releasable To lists. Default is `fiveeyesfirst`.
- Cmd Line Property: `classificationAusReltoOrder`
- Environment Variable: `CLASSIFICATION_AUS_RELTO_ORDER`
- Config File Property: `io.github.trquinn76.classification.aus.relto.order`

##### Production Mode

Determines if the library is operating in Production Mode. When true causes the library to use `PSPFClassification`'s,
otherwise `DevelopmentClassification`'s are used. Defaults to `false`.
- Cmd Line Property: `classificationAusProductionMode`
- Environment Variable: `CLASSIFICATION_AUS_PRODUCTION_MODE`
- Config File Property: `io.github.trquinn76.classification.aus.production.mode`

##### Config File Location

Specifies a User defined Config File.
- Cmd Line Property: `classificationConfigFile`
- Environment Variable: `CLASSIFICATION_CONFIG_FILE`

##### Development UNOFFICIAL String

Determines the String used when displaying the `DEVELOPMENT_UNOFFICIAL` Classification.
- Cmd Line Property: `classificationAusDevelUnofficial`
- Environment Variable: `CLASSIFICATION_AUS_DEVEL_UNOFFICIAL`
- Config File Property: `io.github.trquinn76.classification.aus.development.unofficial.name`

##### Development OFFICIAL String

Determines the String used when displaying the `DEVELOPMENT_OFFICIAL` Classification.
- Cmd Line Property: `classificationAusDevelOfficial`
- Environment Variable: `CLASSIFICATION_AUS_DEVEL_OFFICIAL`
- Config File Property: `io.github.trquinn76.classification.aus.development.official.name`

##### Development OFFICIAL: Sensitive String

Determines the String used when displaying the `DEVELOPMENT_OFFICIAL_SENSITIVE` Classification.
- Cmd Line Property: `classificationAusDevelOfficialSensitive`
- Environment Variable: `CLASSIFICATION_AUS_DEVEL_OFFICIAL_SENSITIVE`
- Config File Property: `io.github.trquinn76.classification.aus.development.official.sensitive.name`

##### Development PROTECTED String

Determines the String used when displaying the `DEVELOPMENT_PROTECTED` Classification.
- Cmd Line Property: `classificationAusDevelProtected`
- Environment Variable: `CLASSIFICATION_AUS_DEVEL_PROTECTED`
- Config File Property: `io.github.trquinn76.classification.aus.development.protected.name`

##### Development SECRET String

Determines the String used when displaying the `DEVELOPMENT_SECRET` Classification.
- Cmd Line Property: `classificationAusDevelSecret`
- Environment Variable: `CLASSIFICATION_AUS_DEVEL_SECRET`
- Config File Property: `io.github.trquinn76.classification.aus.development.secret.name`

##### Development TOP SECRET String

Determines the String used when displaying the `DEVELOPMENT_TOP_SECRET` Classification.
- Cmd Line Property: `classificationAusDevelTopSecret`
- Environment Variable: `CLASSIFICATION_AUS_DEVEL_TOP_SECRET`
- Config File Property: `io.github.trquinn76.classification.aus.development.top.secret.name`

#### Config precedence

The order of precedence for configuration values are:
- Cmd Line Property
- Environment Variable
- Configuration File value
- Default value

When searching configuration files, the library will search for files in the following order:
- file defined via the `classificationConfigFile` command line property. eg:
`java -DclassificationConfigFile=myconfig.properties MyApp`. Or the `CLASSIFICATION_CONFIG_FILE` environment variable.
- `application.properties`: this config file name is used by both **SpringBoot** and **Quarkus**.
- `classification-config.properties`: the developer can create this file anywhere on the classpath and it should be
successfully read in.
- `default-classification-config.properties`: which already exists in the library `JAR` file, and holds the default
values used in this library.

#### Configuring to use real/PSPF Classifications

By default the library will use the `DevelopmentClassification`'s, rather than the real Classifications defined in
`PSPFClassification`. In order to use the `PSPFClassification`'s, it is necessary to set the Production Mode config
value. This can be done via runtime command line property, environment variable or configuration.

eg:
- `java -DclassificationAusProductionMode=true MyApp`
- `CLASSIFICATION_AUS_PRODUCTION_MODE=true`
- in config file `application.properties` set: `io.github.trquinn76.classification.aus.production.mode = true`

## Implementation considerations

### Sets for Lists

There are a number of `List`'s in the `ProtectiveMarkerBuilder` which are implemented as `TreeSet`'s. These `Set`'s are
converted to true `List`'s when the `ProtectiveMarker` is built. Their use ensures that duplicate values are handled
and that the generated `List`'s are in alphabetical order.

### Merging Protective Markings

When using the `merge()` functions in the `Utils` class, it is important to remember that:
- Special Handling Instructions are NOT merged. It is not clear from the source documentation how these values could be
merged. Especially for cases such a Exclusive For instructions for different addressees, it is not clear how that
should be handled, or if that would represent a Breach.
- The returned `ProtectiveMarkerBuilder` returned by each of the `merge()` functions is NOT guaranteed to be in a valid
state, and it may require additional work to be able to build a new `ProtectiveMarker`. In particular Releasable To
lists may be reduced to a single entry of `AUS`.
