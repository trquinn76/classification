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
>         <version>1.0.0</version>
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

IN-CONFIDENCE and SENSITIVE are considered to be Policy and Privacy Classifications.

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

This library assumes that Policy and Privacy Endorsements may be applied to ANY Classification, as I didn't read the Overview
closely enough, and it turns out it DOES say that PnP Endorsements are for PnP Classifications...

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

NS Endorsements should only be applied to NS Classifications..!

#### Accountable Material

#### Sensitive Compartments

#### Releasability
