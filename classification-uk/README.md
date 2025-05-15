# UK Security Classification Model

This project contains a data model for the UK's Security Classification, as defined in the
[Government Security Classification Policy](./documents/GovernmentSecurityClassificationsPolicy/README.md).

This model is derived solely from publicly available documents. The set of documents referenced when designing this
model may be found in [Documents](./documents/README.md). These documents are:
- [Government Security Classification Policy](./documents/GovernmentSecurityClassificationsPolicy/README.md)

> #### Maven Coordinates:
> 
>     <dependency>
>         <groupId>io.github.trquinn76</groupId>
>         <artifactId>classification-uk</artifactId>
>         <version>1.0.0</version>
>     </dependency>

### Purpose

This model exists to deal with the issue that data stored in computer systems frequently needs to maintain the
classification of the data. This is typically done in ad hoc ways, so that every data structure and library stores it's
classification data in different formats. Which then need to be mapped from one format to another, whenever the
different data sets are merged. Also each ad hoc format tends to support only a limited part of the classification marking
standard - leading to issues with converting between formats, and with the periodic need to enhance an existing
format with additional fields.

In this project the model is intended to be comprehensive, and suitable for use as Data Transfer Objects. The models
are based on `record`'s, with `unmodifiable` collections, to ensure immutability. This also makes these models safe
for use in threaded environments.

## The Two purposes of the `ClassificationMarkerBuilder`

### Creating new ClassificationMarker's

To create new `ClassificationMarker`'s, first create an instance of `ClassificationMarkerBuilder`. Populate the fields of the
builder, and then use the `build()` function to create a new `ClassificationMarker`. Use of the builder ensures that the
resulting `ClassificationMarker` is valid.

### Handling Invalid States (particularly in UI's)

In the course of working with Classified data, it is not unusual to need to hold representations of 
`ClassificationMarker`'s in invalid, intermediate states, while the User edits them via the UI. One of the
`ClassificationMarkerBuilder`'s purposes is to be able to hold `ClassificationMarker` information in an invalid state,
while a User is actively editing it via a UI.

## ClassificationMarker structure

The `ClassificationMarker` `record` consists of the following fields:

### UK Prefix

This field is set true when a `UK` prefix should be added to the Marker. From the
[Government Security Classification Policy](./documents/GovernmentSecurityClassificationsPolicy/README.md)
**Prefixes and National Caveats** table:
> ALL assets sent to foreign governments or International Organisations (e.g. the North Atlantic Treaty Organisation
> (NATO)) must be marked with a UK prefix.

This prefix may not provide any value for electronic data, but is included in the library for completeness.

##### REL EU

When releasing data to the EU, along with the `UK` prefix, a releasability marking `REL-EU` should be included.
This marking is added to the `Additional Instructions` when used.

### Classification

The `Classification` for this `ClassificationMarker`. The list of Classifications is derived from 
[Government Security Classification Policy](./documents/GovernmentSecurityClassificationsPolicy/README.md).

The `Classification` is required.

The Classifications are:
- OFFICIAL
- SECRET
- TOP SECRET

The `Classification` record is required to be built with a String which is the `name()` of one of the entries in the
currently configured Classification enumeration.

##### Sensitive Mark

There is an `Additional Mark`, `SENSITIVE`, which may be applied to the OFFICIAL Classification. This indicate OFFICIAL
data which requires additional protections. See 
[Government Security Classification Policy](./documents/GovernmentSecurityClassificationsPolicy/README.md)
**Applying the-SENSITIVE marking**.

##### Default Classifications

By default the library is configured to use a set of `DevelopmentClassification`'s. These are deliberately distinct
from the real set of `Classification`'s (`SecurityClassification`). Configuring `ProductionMode` to be true will cause the
library to start using the real set of `Classification`'s.

`DevelopmentClassification`'s exist to allow development and testing of Classification related logic and data storage
in less secure environments than the final target production environment.

### Additional Markings

Quoting from [Government Security Classification Policy](./documents/GovernmentSecurityClassificationsPolicy/README.md)
** What are Additional Markings? - 22**:

> Additional markings can be added in conjunction with a classification to indicate the nature or source of the
> information, or to limit access to specific user groups.

### Handling Instructions

Provides additional information about how the related data should be handled. Many of the defined instructions are
related to the handling of physical documents, but are still included for completeness.

- RECIPIENTS ONLY - any associated list of recipients may be included in the `Additional Instructions`.
- FOR PUBLIC RELEASE
- [ORGANISATION(S)] USE ONLY - requires a minimum of one organisation.
- HMG USE ONLY
- EMBARGOED - any related date and time should be included in the `Additional Instructions`.

The above listed `Handling Instructions` are found in
[Government Security Classification Policy](./documents/GovernmentSecurityClassificationsPolicy/README.md)
**Handling Instructions** table. Individual Organisations may define additional `Handling Instruction`'s, so
this cannot be seen as a comprehensive list.

### Descriptors

Descriptors are used to identify categories of information with special sensitivities and access restrictions.

- PERSONAL DATA
- LEGAL PROFESSIONAL PRIVILEGE (LPP)
- LEGAL
- MARKET SENSITIVE
- COMMERCIAL
- HR/MANAGEMENT

The above listed `Descriptors` are found in
[Government Security Classification Policy](./documents/GovernmentSecurityClassificationsPolicy/README.md)
**Descriptors** table. Individual Organisations may define additional `Descriptors`, so this cannot be seen as a
comprehensive list.

### Code Words

Quoting from [Government Security Classification Policy](./documents/GovernmentSecurityClassificationsPolicy/README.md)
**Code Words** table:

> Code words provide security cover for a particular asset or event. A code word is a single word expressed in CAPITAL
> letters. They are most commonly applied to SECRET and TOP SECRET assets. Code words are centrally allocated; please
> contact your SSA/SA if you require one for an asset at any tier.

In this library, `Code Words` may be applied to the OFFICIAL Classification, as well as SECRET and TOP SECRET, as it
is not forbidden, which implies it is allowed.

### National Caveats (Eyes Only)

Quoting from [Government Security Classification Policy](./documents/GovernmentSecurityClassificationsPolicy/README.md)
**Prefixes and National Caveats** table:

> National Caveats are used to designate assets of particular sensitivity to the UK or where dissemination must be
> restricted to individuals from specific foreign nations.

Examples are:
- UK EYES ONLY
- FIVE EYES ONLY
- UK/US EYES ONLY

When there are multiple Countries listed in the EYES ONLY (National Caveats) list, they are to be displayed in
alphabetic order.

Countries (or Organisations) should be in all caps. This is not enforced.

Currently this library requires that either `UK` or `FIVE` appears in the list of National Caveats, if they are present.

### Additional Instructions

Contains a collection of Strings which represent additional instructions. May include recipient lists, or embargo dates
and time. Also used for the `REL EU` releasability instruction, used with the `UK` prefix.

## Configuration

##### Production Mode

Determines if the library is operating in Production Mode. When true causes the library to use `SecurityClassification`'s,
otherwise `DevelopmentClassification`'s are used. Defaults to `false`. This configuration value is shared with other
Classification libraries.
- Cmd Line Property: `classificationProductionMode`
- Environment Variable: `CLASSIFICATION_PRODUCTION_MODE`
- Config File Property: `io.github.trquinn76.classification.production.mode`

##### Config File Location

Specifies a User defined Config File. This configuration value is shared with other Classification libraries.
- Cmd Line Property: `classificationConfigFile`
- Environment Variable: `CLASSIFICATION_CONFIG_FILE`

##### Development OFFICIAL String

Determines the String used when displaying the `DEVELOPMENT_OFFICIAL` Classification.
- Cmd Line Property: `classificationUkDevelOfficial`
- Environment Variable: `CLASSIFICATION_UK_DEVEL_OFFICIAL`
- Config File Property: `io.github.trquinn76.classification.uk.development.official.name`

##### SENSITIVE Mark

Determines the String used to display the SENSITIVE mark. This is appended immediately after the OFFICIAL
Classification String with no spaces. The default value is `-SENSITIVE`, where the `-` character is
important, and should not be removed.
- Cmd Line Property: `classificationUkSensitiveMark`
- Environment Variable: `CLASSIFICATION_UK_SENSITIVE_MARK`
- Config File Property: `io.github.trquinn76.classification.uk.sensitive.mark.name`

##### Development SECRET String

Determines the String used when displaying the `DEVELOPMENT_SECRET` Classification.
- Cmd Line Property: `classificationUkDevelSecret`
- Environment Variable: `CLASSIFICATION_UK_DEVEL_SECRET`
- Config File Property: `io.github.trquinn76.classification.uk.development.secret.name`

##### Development TOP SECRET String

Determines the String used when displaying the `DEVELOPMENT_TOP_SECRET` Classification.
- Cmd Line Property: `classificationUkDevelTopSecret`
- Environment Variable: `CLASSIFICATION_UK_DEVEL_TOP_SECRET`
- Config File Property: `io.github.trquinn76.classification.uk.development.top.secret.name`

#### Config precedence

The order of precedence for configuration values are:
- Cmd Line Property (NOTE: not command line variable)
- Environment Variable
- Configuration File value
- Default value

When searching configuration files, the library will search for files in the following order:
- file defined via the `classificationConfigFile` command line property. eg:
`java -DclassificationConfigFile=myconfig.properties MyApp`. Or the `CLASSIFICATION_CONFIG_FILE` environment variable.
- `application.properties`: this config file name is used by both **SpringBoot** and **Quarkus**.
- `classification-config.properties`: the developer can create this file anywhere on the classpath and it should be
successfully read in.
- `uk-default-classification-config.properties`: which already exists in the library `JAR` file, and holds the default
values used in this library.

#### Configuring to use real/Security Classifications

By default the library will use the `DevelopmentClassification`'s, rather than the real Classifications defined in
`SecurityClassification`. In order to use the `SecurityClassification`'s, it is necessary to set the Production Mode config
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

