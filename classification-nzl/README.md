# New Zealand Classification/Protective Marking Model

This project contains a data model for Australian Protective Markings, as defined in the
[Overview](./documents/Overview/README.md).

This model is derived solely from publicly available documents. The set of documents referenced when designing this
model may be found in [Documents](./documents/README.md). These documents are:
- [Overview](./documents/Overview/README.md)
- [Protectively marking information and equipment](./documents/ApplyMarkings/README.md)
- [Classification Handbook](./documents/ClassificationHandbook/README.md)

> #### Maven Coordinates:
> 
>     <dependency>
>         <groupId>io.github.trquinn76</groupId>
>         <artifactId>classification-nzl</artifactId>
>         <version>1.0.1</version>
>     </dependency>

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

## ProtectiveMarker structure

The `ProtectiveMarker` `record` consists of the following fields:

### Classification

The `Classification` for this `ProtectiveMarker`. The list of Classifications is derived from 
[Overview](./documents/Overview/README.md) and a reference to an UNCLASSIFIED value in 
[Protectively marking information and equipment](./documents/ApplyMarkings/README.md).

The `Classification` is required.

The Classifications are:
- UNCLASSIFIED
- IN-CONFIDENCE
- SENSITIVE
- RESTRICTED
- CONFIDENTIAL
- SECRET
- TOP SECRET

##### Policy And Privacy Classifications

IN-CONFIDENCE and SENSITIVE are considered to be Policy and Privacy Classifications.

##### National Security Classifications

RESTRICTED, CONFIDENTIAL, SECRET and TOP SECRET are considered to be National Security Classifications.

The `Classification` record is required to be built with a String which is the `name()` of one of the entries in the
currently configured Classification enumeration.

##### Default Classifications

By default the library is configured to use a set of `DevelopmentClassification`'s. These are deliberately distinct
from the real set of `Classification`'s (`NZLClassification`). Configuring `ProductionMode` to be true will cause the
library to start using the real set of `Classification`'s.

`DevelopmentClassification`'s exist to allow development and testing of Classification related logic and data storage
in less secure environments than the final target production environment.

### Endorsements

Quoting from [Overview](./documents/Overview/README.md) **Endorsements** Section.

> Endorsements warn people that information has special requirements. These are additional markings alongside the 
classification.


### Policy And Privacy Endorsements

In this library Policy and Privacy Endorsements may only be applied to Policy And Privacy Classifications.
That is `IN-CONFIDENCE` and `SENSITIVE` Classifications.

A list of Policy and Privacy Endorsements, wrapped with an optional String, which is required for some Endorsements. The
list of supported Policy and Privacy Endorsements is found in the [Overview](./documents/Overview/README.md):
- APPOINTMENTS
- BUDGET
- CABINET
- COMMERCIAL
- DEPARTMENT USE ONLY
- EMBARGOED FOR RELEASE
- EVALUATE
- HONOURS
- LEGAL PRIVILEGE
- MEDICAL
- STAFF
- POLICY
- TO BE REVIEWED ON

#### DEPARTMENT USE ONLY

When the Endorsement is DEPARTMENT USE ONLY, the associated String is required to be populated with a comma separated
list of those Departments which may use the associated data. The `ProtectiveMarkerBuilder` API assists with populating
this String, with one or more Departments.

#### EMBARGOED FOR RELEASE

When the Endorsement is EMBARGOED FOR RELEASE, the associated String is required to be populated with the release date
in the Configured format. The default format gives the date and time to the minute.

It is not wholly clear from the documentation that this Endorsement requires a specified Date. However, this library
is currently requiring one.

A `LocalDateTime` object is used to represent the time in the `ProtectedMarkerBuilder`. When it matters it should be 
assumed that the time is in the New Zealand time zone. Either standard time (UTC+12), or daylight time (UTC+13).

#### TO BE REVIEWED ON

When the Endorsement is TO BE REVIEWED ON, the associated String is required to be populated with the review date
in the Configured format. The default format gives the date and time to the minute, which is likely more precise
than this Endorsement needs.

A `LocalDateTime` object is used to represent the time in the `ProtectedMarkerBuilder`. When it matters it should be 
assumed that the time is in the New Zealand time zone. Either standard time (UTC+12), or daylight time (UTC+13).

### National Security Endorsements

In this library National Security Endorsements may only be applied to National Security Classifications.
That is `RESTRICTED`, `CONFIDENTIAL`, `SECRET` and `TOP SECRET` Classifications.

#### Controls

##### Accountable Material

Indicates material which requires strict control over access and regular auditing. `TOP SECRET` material is Accountable
Material by default.

In this library, the `TOP SECRET` classification is required to have the `ACCOUNTABLE MATERIAL` control set. It is
represented with a `boolean` value.

##### Sensitive Compartments

> A sensitive compartmented marking is a word indicating that the information is in a specific need-to-know compartment.
> This word could be a codeword or ‘Sensitive Compartmented Information (SCI)’.

Sensitive Compartments is a list of Strings. It may be empty.

#### Dissemination

##### Additonal Dissemination Marks

In the [Protectively marking information and equipment](./documents/ApplyMarkings/README.md) document, section
**Applying National Security endorsement markings** there is a reference to an `ORCON` dissemination mark.

> The format is:
> 
> CLASSIFICATION//CONTROL 1/CONTROL 2//DISSEMINATION 1/DISSEMINATION 2
> 
> Example 2:
> 
> **TOP SECRET//<SCI CODEWORD>//ORCON/REL TO NZL, FVEY**

This dissemination mark is not mentioned anywhere else in the publicly facing documentation which has been found and
adopted as the source of truth for this library. It is not clear if this marking is actually in use, or if it is now
obsolete. It is also unclear what other markings might be in use which are not documented publicly.

To handle this uncertainty this library allows arbitrary strings to be added to the `NationalSecurityEndorsements`
`record` as additional Dissemination marks.

##### Releasability

The `ReleasabilityMarking` represents the releasability of the `ProtectiveMarker`. It is made up of a
`ReleasabilityTypes` and a releasable to list.

The `ReleasabilityTypes`, as defined in [Overview](./documents/Overview/README.md) section **National
security endorsement markings** are:
- NZEO
- RELTO

The `releasable to` list of the `ReleasabilityMarking` is only to be populated if the `ReleasabilityTypes` is RELTO.

When the Releasable type is RELTO, then the `releasable to` list should be populated with a minimum of 2 values, one
of which must be `NZL`. `Releasable to` lists are ordered alphabetically, with `NZL` leading.

`Releasable to` lists are expected to be populated with country trigraphs, as defined in the 
`International Standard ISO 3166-1:2013 alpha3 Codes`. This is not enforced, except for requiring that `NZL` be present.

In this library the preferred way to represent the Five Eyes in a RELTO list is to explicitly list each country. eg:
`NZL, AUS, CAN, GBR, USA`. Use of Strings like `FVEY` is not prevented, but `NZL` will always be required.

If there is no Releasability, then the `ReleasabilityMarking` field will be null.

## Configuration

##### Production Mode

Determines if the library is operating in Production Mode. When true causes the library to use `NZLClassification`'s,
otherwise `DevelopmentClassification`'s are used. Defaults to `false`. This configuration value is shared with other
Classification libraries.
- Cmd Line Property: `classificationProductionMode`
- Environment Variable: `CLASSIFICATION_PRODUCTION_MODE`
- Config File Property: `io.github.trquinn76.classification.production.mode`

##### Config File Location

Specifies a User defined Config File. This configuration value is shared with other Classification libraries.
- Cmd Line Property: `classificationConfigFile`
- Environment Variable: `CLASSIFICATION_CONFIG_FILE`

##### Development UNCLASSIFIED String

Determines the String used when displaying the `DEVELOPMENT_UNCLASSIFIED` Classification.
- Cmd Line Property: `classificationNzlDevelUnclassified`
- Environment Variable: `CLASSIFICATION_NZL_DEVEL_UNCLASSIFIED`
- Config File Property: `io.github.trquinn76.classification.nzl.development.unclassified.name`

##### Development IN-CONFIDENCE String

Determines the String used when displaying the `DEVELOPMENT_IN_CONFIDENCE` Classification.
- Cmd Line Property: `classificationNzlDevelInConfidence`
- Environment Variable: `CLASSIFICATION_NZL_DEVEL_IN_CONFIDENCE`
- Config File Property: `io.github.trquinn76.classification.nzl.development.in.confidence.name`

##### Development SENSITIVE String

Determines the String used when displaying the `DEVELOPMENT_SENSITIVE` Classification.
- Cmd Line Property: `classificationNzlDevelSensitive`
- Environment Variable: `CLASSIFICATION_NZL_DEVEL_SENSITIVE`
- Config File Property: `io.github.trquinn76.classification.nzl.development.sensitive.name`

##### Development RESTRICTED String

Determines the String used when displaying the `DEVELOPMENT_RESTRICTED` Classification.
- Cmd Line Property: `classificationNzlDevelRestricted`
- Environment Variable: `CLASSIFICATION_NZL_DEVEL_RESTRICTED`
- Config File Property: `io.github.trquinn76.classification.nzl.development.restricted.name`

##### Development CONFIDENTIAL String

Determines the String used when displaying the `DEVELOPMENT_CONFIDENTIAL` Classification.
- Cmd Line Property: `classificationNzlDevelConfidential`
- Environment Variable: `CLASSIFICATION_NZL_DEVEL_CONFIDENTIAL`
- Config File Property: `io.github.trquinn76.classification.nzl.development.confidential.name`

##### Development SECRET String

Determines the String used when displaying the `DEVELOPMENT_SECRET` Classification.
- Cmd Line Property: `classificationNzlDevelSecret`
- Environment Variable: `CLASSIFICATION_NZL_DEVEL_SECRET`
- Config File Property: `io.github.trquinn76.classification.nzl.development.secret.name`

##### Development TOP SECRET String

Determines the String used when displaying the `DEVELOPMENT_TOP_SECRET` Classification.
- Cmd Line Property: `classificationNzlDevelTopSecret`
- Environment Variable: `CLASSIFICATION_NZL_DEVEL_TOP_SECRET`
- Config File Property: `io.github.trquinn76.classification.nzl.development.top.secret.name`

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
- `nzl-default-classification-config.properties`: which already exists in the library `JAR` file, and holds the default
values used in this library.

#### Configuring to use real/NZL Classifications

By default the library will use the `DevelopmentClassification`'s, rather than the real Classifications defined in
`NZLClassification`. In order to use the `NZLClassification`'s, it is necessary to set the Production Mode config
value. This can be done via runtime command line property, environment variable or configuration.

eg:
- `java -DclassificationProductionMode=true MyApp`
- `CLASSIFICATION_PRODUCTION_MODE=true`
- in config file `application.properties` set: `io.github.trquinn76.classification.production.mode = true`

## Implementation Considerations

### Sets for Lists

There are a number of `List`'s in the `ProtectiveMarkerBuilder` which are implemented as `TreeSet`'s. These `Set`'s are
converted to true `List`'s when the `ProtectiveMarker` is built. Their use ensures that duplicate values are handled
and that the generated `List`'s are in alphabetical order.

### No Endorsements for UNCLASSIFIED

This library does not allow Endorsements to be applied when the Classification is `UNCLASSIFIED`. The `UNCLASSIFIED`
Classification mainly exists for those cases where data structures require a Classification, but the data is not
Classified.
