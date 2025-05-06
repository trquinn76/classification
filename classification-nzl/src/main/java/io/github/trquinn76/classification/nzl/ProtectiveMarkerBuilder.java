package io.github.trquinn76.classification.nzl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;

import io.github.trquinn76.classification.nzl.model.Classification;
import io.github.trquinn76.classification.nzl.model.NationalSecurityEndorsements;
import io.github.trquinn76.classification.nzl.model.PolicyAndPrivacyEndorsementMarking;
import io.github.trquinn76.classification.nzl.model.PolicyAndPrivacyEndorsements;
import io.github.trquinn76.classification.nzl.model.ProtectiveMarker;
import io.github.trquinn76.classification.nzl.model.ReleasabilityMarking;
import io.github.trquinn76.classification.nzl.model.ReleasabilityTypes;

/**
 * A builder for {@link ProtectiveMarker}'s.
 * 
 * This class performs 2 primary jobs. One is obviously supporting the Builder Pattern for {@link ProtectiveMarker}'s.
 * The second is being an object which can hold the values of a {@link ProtectiveMarker} in an invalid state,
 * specifically in support of UI components.
 * <p>
 * A number of lists in the {@link ProtectiveMarker}'s data structure are modeled with Sets in this class.
 * This allows sorting and elimination of duplicate values. When the {@link ProtectiveMarkerBuilder} performs the
 * actual build, these Sets are converted to Lists as necessary.
 */
public class ProtectiveMarkerBuilder {

    private static final Logger LOGGER = Logger.getLogger(ProtectiveMarkerBuilder.class.getCanonicalName());

    private Classification classification = null;
    private Set<PolicyAndPrivacyEndorsements> policyAndPrivacyEndorsements = new TreeSet<>();
    private Set<String> departmentUseOnlyDepartments = new TreeSet<>();
    private LocalDateTime embargoedForReleaseTime = null;
    private LocalDateTime toBeReviewedOnTime = null;
    private boolean accountableMaterial = false;
    private Set<String> sensitiveCompartments = new TreeSet<>();
    private Set<String> disseminationMarks = new TreeSet<>();
    private ReleasabilityTypes releasablityType = null;
    private Set<String> releasableToList = new TreeSet<>(ClassificationConfig.releasableToOrder());

    public ProtectiveMarkerBuilder() {

    }

    /**
     * A copy constructor.
     * 
     * Creates an instance of {@link ProtectiveMarkerBuilder} which is populated with the values from the given
     * {@code protectiveMarker} parameter.
     * 
     * @param marker the {@link ProtectiveMarker} with which to populate the builder.
     */
    public ProtectiveMarkerBuilder(ProtectiveMarker marker) {
        this.classification = marker.classification();

        if (marker.hasPolicyAndPrivacyEndorsements()) {
            for (PolicyAndPrivacyEndorsementMarking pnpEndorsement : marker.policyAndPrivacyEndorsements()) {
                this.policyAndPrivacyEndorsements.add(pnpEndorsement.endorsement());
                String timeOrUseOnlyStr = pnpEndorsement.timeOrUseOnlyValue();
                if (timeOrUseOnlyStr != null) {
                    switch (pnpEndorsement.endorsement()) {
                    case EMBARGOED_FOR_RELEASE: {
                        this.embargoedForReleaseTime = LocalDateTime.parse(timeOrUseOnlyStr,
                                ClassificationConfig.dateTimeFormatter());
                        break;
                    }
                    case TO_BE_REVIEWED_ON: {
                        this.toBeReviewedOnTime = LocalDateTime.parse(timeOrUseOnlyStr,
                                ClassificationConfig.dateTimeFormatter());
                        break;
                    }
                    case DEPARTMENT_USE_ONLY: {
                        this.departmentUseOnlyDepartments.addAll(parseDepartments(timeOrUseOnlyStr));
                        break;
                    }
                    default:
                        throw new IllegalArgumentException(
                                "May not have a populated 'timeOrUseOnlyValue' for Policy and Privacy Endorsement "
                                        + pnpEndorsement.endorsement().toString());

                    }
                }
            }
        }

        if (marker.hasNationalSecurityEndorsements()) {
            this.accountableMaterial = marker.nationalSecurityEndorsements().accountableMaterial();
            this.sensitiveCompartments.addAll(marker.nationalSecurityEndorsements().sensitiveCompartments());
            this.disseminationMarks.addAll(marker.nationalSecurityEndorsements().disseminationMarks());

            if (marker.nationalSecurityEndorsements().releasability() != null) {
                this.releasablityType = marker.nationalSecurityEndorsements().releasability().type();
                this.releasableToList.addAll(marker.nationalSecurityEndorsements().releasability().releasableToList());
            }
        }
        
    }

    /**
     * Clears all values from the builder performing a complete reset.
     * 
     * @return this for function chaining.
     */
    public ProtectiveMarkerBuilder clear() {
        setClassification(null);
        clearPolicyAndPrivacyEndorsements();
        return clearNationalSecurityEndorsements();
    }
    
    /**
     * Clears National Security related Endorsements from the builder.
     *  
     * @return this for function chaining.
     */
    public ProtectiveMarkerBuilder clearNationalSecurityEndorsements() {
        setAccountableMaterial(false);
        clearSensitiveCompartments();
        clearDisseminationMarks();
        clearReleasability();
        return this;
    }

    /**
     * Sets the {@link Classification}.
     * 
     * @param classification the {@link Classification} to set. May be null.
     * @return this for function chaining.
     */
    public ProtectiveMarkerBuilder setClassification(Classification classification) {
        this.classification = classification;
        return this;
    }

    /**
     * Gets the {@link Classification} set in this builder.
     * 
     * @return the current {@link Classification}.
     */
    public Classification getClassification() {
        return this.classification;
    }

    /**
     * Sets the {@link Classification} to UNCLASSIFIED.
     * 
     * @return this for function chaining.
     */
    public ProtectiveMarkerBuilder unclassified() {
        this.classification = Classification.unclassified();
        return this;
    }

    /**
     * Sets the {@link Classification} to IN-CONFIDENCE.
     * 
     * @return this for function chaining.
     */
    public ProtectiveMarkerBuilder inConfidence() {
        this.classification = Classification.inConfidence();
        return this;
    }

    /**
     * Sets the {@link Classification} to SENSITIVE.
     * 
     * @return this for function chaining.
     */
    public ProtectiveMarkerBuilder sensitive() {
        this.classification = Classification.sensitive();
        return this;
    }

    /**
     * Sets the {@link Classification} to RESTRICTED.
     * 
     * @return this for function chaining.
     */
    public ProtectiveMarkerBuilder restricted() {
        this.classification = Classification.restricted();
        return this;
    }

    /**
     * Sets the {@link Classification} to CONFIDENTIAL.
     * 
     * @return this for function chaining.
     */
    public ProtectiveMarkerBuilder confidental() {
        this.classification = Classification.confidential();
        return this;
    }

    /**
     * Sets the {@link Classification} to SECRET.
     * 
     * @return this for function chaining.
     */
    public ProtectiveMarkerBuilder secret() {
        this.classification = Classification.secret();
        return this;
    }

    /**
     * Sets the {@link Classification} to TOP SECRET, and also sets Accountable Material to true.
     * 
     * @return this for function chaining.
     */
    public ProtectiveMarkerBuilder topSecret() {
        this.classification = Classification.topSecret();
        // Accountable Material must be set for TOP SECRET.
        return accountableMaterial();
    }

    /**
     * Sets an Accountable Material flag.
     * 
     * When the {@link Classification} is TOP SECRET, Accountable Material is required to be true.
     * 
     * @param accountableMaterial the boolean indicating if this {@link ProtectiveMarker} should indicate Accountable Material.
     * @return this for function chaining.
     */
    public ProtectiveMarkerBuilder setAccountableMaterial(boolean accountableMaterial) {
        this.accountableMaterial = accountableMaterial;
        return this;
    }

    /**
     * Sets Accountable Material true.
     * 
     * @return this for function chaining.
     */
    public ProtectiveMarkerBuilder accountableMaterial() {
        return setAccountableMaterial(true);
    }

    /**
     * Returns the value of the Accountable Material boolean in this builder.
     * 
     * @return boolean
     */
    public boolean hasAccountableMaterial() {
        return this.accountableMaterial;
    }

    /**
     * Sets the Sensitive Compartments in this builder.
     * 
     * Will remove any existing values, and populate the compartments list with the given values.
     * 
     * @param sensitiveCompartments a Collection of Compartments. May not be null. May be empty.
     * @return this for function chaining.
     */
    public ProtectiveMarkerBuilder setSensitiveCompartments(Collection<String> sensitiveCompartments) {
        Objects.requireNonNull(sensitiveCompartments);
        clearSensitiveCompartments();
        this.sensitiveCompartments.addAll(sensitiveCompartments);
        return this;
    }

    /**
     * Adds the given compartment to the Sensitive Compartment List.
     * 
     * @param sensitiveCompartment a compartment String. May not be null.
     * @return this for function chaining.
     */
    public ProtectiveMarkerBuilder addSensitiveCompartment(String sensitiveCompartment) {
        Objects.requireNonNull(sensitiveCompartment);
        this.sensitiveCompartments.add(sensitiveCompartment);
        return this;
    }

    /**
     * Gets a copy of the current Set of Compartment Strings.
     * 
     * @return a Set of String, which is a copy of the current Compartments.
     */
    public Set<String> getSensitiveCompartments() {
        return new TreeSet<>(this.sensitiveCompartments);
    }

    /**
     * Removes existing compartments from the Sensitive Compartments list.
     * 
     * @return this for function chaining.
     */
    public ProtectiveMarkerBuilder clearSensitiveCompartments() {
        this.sensitiveCompartments.clear();
        return this;
    }

    /**
     * Allows setting Compartments via a function array parameter.
     * 
     * @param compartments an array of compartments. May not be null. May be empty.
     * @return this for function chaining.
     */
    public ProtectiveMarkerBuilder sensitiveCompartments(String... compartments) {
        return setSensitiveCompartments(Arrays.asList(compartments));
    }
    
    /**
     * Sets Dissemination Marks in this builder.
     * 
     *  Will remove any existing values, and populate the dissemination list with the given values.
     * 
     * @param disseminationMarks the dissemination marks to set on the builder.
     * @return this for function chaining.
     */
    public ProtectiveMarkerBuilder setDisseminationMarks(Collection<String> disseminationMarks) {
        Objects.requireNonNull(disseminationMarks);
        this.disseminationMarks.clear();
        this.disseminationMarks.addAll(disseminationMarks);
        return this;
    }
    
    /**
     * Adds the given dissemination mark to the builder's list of dissemination marks.
     * 
     * @param disseminationMark the dissemination mark to add.
     * @return this for function chaining.
     */
    public ProtectiveMarkerBuilder addDisseminationMark(String disseminationMark) {
        this.disseminationMarks.add(disseminationMark);
        return this;
    }
    
    /**
     * Gets a copy of the current dissemination marks.
     * 
     * @return a Set which is a copy of the existing list in the builder.
     */
    public Set<String> getDisseminationMarks() {
        return new TreeSet<>(this.disseminationMarks);
    }
    
    /**
     * Clears all existing dissemination marks from the builder.
     * 
     * @return this for function chaining.
     */
    public ProtectiveMarkerBuilder clearDisseminationMarks() {
        this.disseminationMarks.clear();
        return this;
    }
    
    /**
     * Sets the disseminations marks in the builder to the values in the given array.
     * 
     * @param disseminationMarks an array of dissemination marks to set.
     * @return this for function chaining.
     */
    public ProtectiveMarkerBuilder disseminationMarks(String... disseminationMarks) {
        return setDisseminationMarks(Arrays.asList(disseminationMarks));
    }

    /**
     * Sets the {@link ReleasabilityTypes}.
     * 
     * If the {@link ReleasabilityTypes} is RELTO, then this function will ensure that the releasableToList contains at least 'NZL'.
     * 
     * @param type the {@link ReleasabilityTypes}. May be null.
     * @return this for function chaining.
     */
    public ProtectiveMarkerBuilder setReleasability(ReleasabilityTypes type) {
        this.releasablityType = type;
        if (ReleasabilityTypes.RELTO == type) {
            // Having set releasability type to RELTO am ensuring that NZL exists in the
            // Releasable To list.
            this.releasableToList.add(Utils.NZL);
        }
        return this;
    }

    /**
     * Gets the current {@link ReleasabilityTypes}.
     * 
     * @return the current {@link ReleasabilityTypes}. May be null.
     */
    public ReleasabilityTypes getReleasablity() {
        return this.releasablityType;
    }

    /**
     * Clears Releasability information from the builder.
     * 
     * The {@link ReleasabilityTypes} is set to null, and the Releasable To List is cleared.
     * 
     * @return this for function chaining.
     */
    public ProtectiveMarkerBuilder clearReleasability() {
        this.releasablityType = null;
        return this.clearReleasableToList();
    }

    /**
     * Sets the Releasable To List to the values in the given parameter.
     * 
     * Any existing Releasable To values are cleared, and then replaced.
     * <p>
     * To be valid a Releasable To list must contain a minimum of 2 values, one of which must be 'NZL'.
     * <p>
     * The country codes in Releasable To lists should be from 'International Standard ISO 3166-1:2013 alpha3 Codes'.
     * This is not enforced.
     * 
     * @param releasableToList the list of countries to which release is allowed. May not be null.
     * @return this for function chaining.
     */
    public ProtectiveMarkerBuilder setReleasableToList(Collection<String> releasableToList) {
        Objects.requireNonNull(releasableToList);
        this.releasableToList.clear();
        this.releasableToList.addAll(releasableToList);
        return this;
    }

    /**
     * Add the given country code to the Releasable To List.
     * 
     * Country codes in Releasable To lists should be from 'International Standard ISO 3166-1:2013 alpha3 Codes'.
     * This is not enforced.
     * 
     * @param country the country code to add to the releasable to list. May not be null.
     * @return this for function chaining.
     */
    public ProtectiveMarkerBuilder addReleasableToCountry(String country) {
        Objects.requireNonNull(country);
        this.releasableToList.add(country);
        return this;
    }

    /**
     * Returns a copy of the Set containing the current releasable to list.
     * 
     * This copy will have the same {@link java.util.Comparator} set as the internal Set.
     * 
     * @return a Set of country codes, using the configured releasable to comparator.
     */
    public Set<String> getReleasableToList() {
        Set<String> releasableToList = new TreeSet<>(ClassificationConfig.releasableToOrder());
        releasableToList.addAll(this.releasableToList);
        return releasableToList;
    }

    /**
     * Clears existing values from the releasable to list.
     * 
     * @return this for function chaining.
     */
    public ProtectiveMarkerBuilder clearReleasableToList() {
        this.releasableToList.clear();
        return this;
    }

    /**
     * Sets the {@link ReleasabilityTypes} to NZEO, and clears any values from the releasable to list.
     * 
     * @return this for function chaining.
     */
    public ProtectiveMarkerBuilder nzeo() {
        this.releasablityType = ReleasabilityTypes.NZEO;
        this.releasableToList.clear();
        return this;
    }

    /**
     * Sets the {@link ReleasabilityTypes} to RELTO, and populates the releasable to list with the given array of country codes.
     * 
     * Country codes in Releasable To lists should be from 'International Standard ISO 3166-1:2013 alpha3 Codes'.
     * This is not enforced.
     * 
     * @param releasableToList a String array of country codes. May not be null. May be empty.
     * @return this for function chaining.
     */
    public ProtectiveMarkerBuilder relTo(String... releasableToList) {
        this.releasablityType = ReleasabilityTypes.RELTO;
        this.releasableToList.clear();
        this.releasableToList.addAll(Arrays.asList(releasableToList));
        return this;
    }

    /**
     * Sets the {@link PolicyAndPrivacyEndorsements} on the builder.
     * 
     * Existing {@link PolicyAndPrivacyEndorsements} are removed from the builder, and the newly provided values are set.
     * 
     * @param endorsements the set of {@link PolicyAndPrivacyEndorsements} to set. May not be null. May be empty.
     * @return this for function chaining.
     */
    public ProtectiveMarkerBuilder setPolicyAndPrivacyEndorsements(
            Collection<PolicyAndPrivacyEndorsements> endorsements) {
        Objects.requireNonNull(endorsements);
        this.policyAndPrivacyEndorsements.clear();
        this.policyAndPrivacyEndorsements.addAll(endorsements);
        return this;
    }

    /**
     * Adds the given {@link PolicyAndPrivacyEndorsements} to the current set of {@link PolicyAndPrivacyEndorsements}.
     * 
     * @param endorsement the {@link PolicyAndPrivacyEndorsements} to add. May not be null.
     * @return this for function chaining.
     */
    public ProtectiveMarkerBuilder addPolicyAndPrivacyEndorsement(PolicyAndPrivacyEndorsements endorsement) {
        Objects.requireNonNull(endorsement);
        this.policyAndPrivacyEndorsements.add(endorsement);
        return this;
    }

    /**
     * Gets a copy of the current Set of {@link PolicyAndPrivacyEndorsements}'s.
     * 
     * @return a copy of the Set of {@link PolicyAndPrivacyEndorsements}'s.
     */
    public Set<PolicyAndPrivacyEndorsements> getPolicyAndPrivacyEndorsements() {
        return new TreeSet<>(this.policyAndPrivacyEndorsements);
    }

    /**
     * Clears existing {@link PolicyAndPrivacyEndorsements} from the builder.
     * 
     * @return this for function chaining.
     */
    public ProtectiveMarkerBuilder clearPolicyAndPrivacyEndorsements() {
        this.policyAndPrivacyEndorsements.clear();
        this.embargoedForReleaseTime = null;
        this.toBeReviewedOnTime = null;
        return this.clearDepartmentUseOnly();
    }

    /**
     * Sets a {@link LocalDateTime} for the EMBARGOED_FOR_RELEASE Endorsement.
     * 
     * When it matters it should be assumed that the time is in the New Zealand time
     * zone. Either standard time (UTC+12), or daylight time (UTC+13).
     * 
     * @param dateTime the {@link LocalDateTime} at which release will occur. May be null.
     * @return this for function chaining.
     */
    public ProtectiveMarkerBuilder setEmbargoedForReleaseTime(LocalDateTime dateTime) {
        this.embargoedForReleaseTime = dateTime;
        return this;
    }

    /**
     * Sets a {@link LocalDateTime} for the TO_BE_REVIEWED_ON Endorsement.
     * 
     * When it matters it should be assumed that the time is in the New Zealand time
     * zone. Either standard time (UTC+12), or daylight time (UTC+13).
     * 
     * @param dateTime the {@link LocalDateTime} at which time a review should be performed. May be null.
     * @return this for function chaining.
     */
    public ProtectiveMarkerBuilder setToBeReviewedOnTime(LocalDateTime dateTime) {
        this.toBeReviewedOnTime = dateTime;
        return this;
    }

    /**
     * Sets the Department names for the DEPARTMENT_USE_ONLY Endorsement.
     * 
     * The documentation does indicate that multiple Departments is valid, so the library supports that.
     * 
     * @param departments a Collection of Departments. May not be null. May be empty.
     * @return this for function chaining.
     */
    public ProtectiveMarkerBuilder setDepartmentUseOnlyDepartments(Collection<String> departments) {
        Objects.requireNonNull(departments);
        this.departmentUseOnlyDepartments.clear();
        this.departmentUseOnlyDepartments.addAll(departments);
        return this;
    }

    /**
     * Adds a Department to the list of Departments.
     * 
     * @param department a Department name. May not be null.
     * @return this for function chaining.
     */
    public ProtectiveMarkerBuilder addDepartmentUseOnly(String department) {
        Objects.requireNonNull(department);
        this.departmentUseOnlyDepartments.add(department);
        return this;
    }

    /**
     * Removes existing Departments from list of Departments.
     * 
     * @return this for function chaining.
     */
    public ProtectiveMarkerBuilder clearDepartmentUseOnly() {
        this.departmentUseOnlyDepartments.clear();
        return this;
    }

    /**
     * Adds the {@link PolicyAndPrivacyEndorsements} APPOINTMENTS to the builder.
     * 
     * @return this for function chaining.
     */
    public ProtectiveMarkerBuilder appointments() {
        this.policyAndPrivacyEndorsements.add(PolicyAndPrivacyEndorsements.APPOINTMENTS);
        return this;
    }

    /**
     * Adds the {@link PolicyAndPrivacyEndorsements} BUDGET to the builder.
     * 
     * @return this for function chaining.
     */
    public ProtectiveMarkerBuilder budget() {
        this.policyAndPrivacyEndorsements.add(PolicyAndPrivacyEndorsements.BUDGET);
        return this;
    }

    /**
     * Adds the {@link PolicyAndPrivacyEndorsements} CABINET to the builder.
     * 
     * @return this for function chaining.
     */
    public ProtectiveMarkerBuilder cabinet() {
        this.policyAndPrivacyEndorsements.add(PolicyAndPrivacyEndorsements.CABINET);
        return this;
    }

    /**
     * Adds the {@link PolicyAndPrivacyEndorsements} COMMERCIAL to the builder.
     * 
     * @return this for function chaining.
     */
    public ProtectiveMarkerBuilder commercial() {
        this.policyAndPrivacyEndorsements.add(PolicyAndPrivacyEndorsements.COMMERCIAL);
        return this;
    }

    /**
     * Adds the {@link PolicyAndPrivacyEndorsements} DEPARTMENT_USE_ONLY to the builder, along with the given array of Departments.
     * 
     * Any existing Department values are cleared, and replaced.
     * 
     * @param departments an array of department names. May not be null. May be empty.
     * @return this for function chaining.
     */
    public ProtectiveMarkerBuilder departmentUseOnly(String... departments) {
        this.policyAndPrivacyEndorsements.add(PolicyAndPrivacyEndorsements.DEPARTMENT_USE_ONLY);
        this.departmentUseOnlyDepartments.clear();
        this.departmentUseOnlyDepartments.addAll(Arrays.asList(departments));
        return this;
    }

    /**
     * Adds the {@link PolicyAndPrivacyEndorsements} EMBARGOED_FOR_RELEASE to the builder, along with the given embargoed date.
     * 
     * When it matters it should be assumed that the time is in the New Zealand time
     * zone. Either standard time (UTC+12), or daylight time (UTC+13).
     * 
     * @param dateTime a {@link LocalDateTime} representing when the embargo should end. May not be null.
     * @return this for function chaining.
     */
    public ProtectiveMarkerBuilder embargoedForRelease(LocalDateTime dateTime) {
        Objects.requireNonNull(dateTime);
        this.policyAndPrivacyEndorsements.add(PolicyAndPrivacyEndorsements.EMBARGOED_FOR_RELEASE);
        this.embargoedForReleaseTime = dateTime;
        return this;
    }

    /**
     * Adds the {@link PolicyAndPrivacyEndorsements} EVALUATE to the builder.
     * 
     * @return this for function chaining.
     */
    public ProtectiveMarkerBuilder evaluate() {
        this.policyAndPrivacyEndorsements.add(PolicyAndPrivacyEndorsements.EVALUATE);
        return this;
    }

    /**
     * Adds the {@link PolicyAndPrivacyEndorsements} HONOURS to the builder.
     * 
     * @return this for function chaining.
     */
    public ProtectiveMarkerBuilder honours() {
        this.policyAndPrivacyEndorsements.add(PolicyAndPrivacyEndorsements.HONOURS);
        return this;
    }

    /**
     * Adds the {@link PolicyAndPrivacyEndorsements} LEGAL_PRIVILEGE to the builder.
     * 
     * @return this for function chaining.
     */
    public ProtectiveMarkerBuilder legalPrivilege() {
        this.policyAndPrivacyEndorsements.add(PolicyAndPrivacyEndorsements.LEGAL_PRIVILEGE);
        return this;
    }

    /**
     * Adds the {@link PolicyAndPrivacyEndorsements} MEDICAL to the builder.
     * 
     * @return this for function chaining.
     */
    public ProtectiveMarkerBuilder medical() {
        this.policyAndPrivacyEndorsements.add(PolicyAndPrivacyEndorsements.MEDICAL);
        return this;
    }

    /**
     * Adds the {@link PolicyAndPrivacyEndorsements} STAFF to the builder.
     * 
     * @return this for function chaining.
     */
    public ProtectiveMarkerBuilder staff() {
        this.policyAndPrivacyEndorsements.add(PolicyAndPrivacyEndorsements.STAFF);
        return this;
    }

    /**
     * Adds the {@link PolicyAndPrivacyEndorsements} POLICY to the builder.
     * 
     * @return this for function chaining.
     */
    public ProtectiveMarkerBuilder policy() {
        this.policyAndPrivacyEndorsements.add(PolicyAndPrivacyEndorsements.POLICY);
        return this;
    }

    /**
     * Adds the {@link PolicyAndPrivacyEndorsements} TO_BE_REVIEWED_ON to the builder, along with the given review date.
     * 
     * When it matters it should be assumed that the time is in the New Zealand time
     * zone. Either standard time (UTC+12), or daylight time (UTC+13).
     * 
     * @param dateTime a {@link LocalDateTime} representing when the review should occur.
     * @return this for function chaining.
     */
    public ProtectiveMarkerBuilder toBeReviewedOn(LocalDateTime dateTime) {
        Objects.requireNonNull(dateTime);
        this.policyAndPrivacyEndorsements.add(PolicyAndPrivacyEndorsements.TO_BE_REVIEWED_ON);
        this.toBeReviewedOnTime = dateTime;
        return this;
    }
    
    /**
     * Returns true if the builder contains Policy and Privacy Endorsements.
     * 
     * @return true if there are Policy and Privacy Endorsements in the builder, false otherwise.
     */
    public boolean hasPolicyAndPrivacyEndorsements() {
        return !this.policyAndPrivacyEndorsements.isEmpty();
    }
    
    /**
     * Returns true if the builder contains National Security Endorsements.
     * 
     * @return true if there are National Security Endorsements in the builder, false otherwise.
     */
    public boolean hasNationalSecurityEndorsements() {
        return this.accountableMaterial || !this.sensitiveCompartments.isEmpty() || !this.disseminationMarks.isEmpty()
                || this.releasablityType != null;
    }

    /**
     * Used to determine if the builder is in a valid state, and able to build a {@link ProtectiveMarker}.
     * 
     * @return a list of String, which reports on invalid parts of the builder which would need to be fixed in order
     *      to perform a build. If there are no problems, and the builder is in a valid state, this list is empty.
     */
    public List<String> isValid() {
        List<String> report = new ArrayList<>();

        checkClassificationValid(report);
        checkPolicyAndPrivacyEndorementsValid(report);
        checkSecurityEndorsements(report);

        return report;
    }

    /**
     * Builds a new instance of {@link ProtectiveMarker} based on the fields in the builder.
     * 
     * @return a new instance of {@link ProtectiveMarker}.
     * @throws IllegalStateException if the builder is not in a valid state, then this exception is thrown.
     */
    public ProtectiveMarker build() {
        List<String> report = isValid();

        if (report.size() > 0) {
            LOGGER.severe("Do not have valid values to build a ProtectiveMarking.");
            for (String line : report) {
                LOGGER.severe(line);
            }
            throw new IllegalStateException("Invalid state, cannot build Protective Marking: " + report.get(0));
        }

        List<PolicyAndPrivacyEndorsementMarking> pnpEndorsementList = new ArrayList<>();
        for (PolicyAndPrivacyEndorsements pnpEndorsement : this.policyAndPrivacyEndorsements) {
            PolicyAndPrivacyEndorsementMarking marking = null;
            if (pnpEndorsement == PolicyAndPrivacyEndorsements.EMBARGOED_FOR_RELEASE) {
                marking = new PolicyAndPrivacyEndorsementMarking(pnpEndorsement,
                        ClassificationConfig.dateTimeFormatter().format(this.embargoedForReleaseTime));
            } else if (pnpEndorsement == PolicyAndPrivacyEndorsements.TO_BE_REVIEWED_ON) {
                marking = new PolicyAndPrivacyEndorsementMarking(pnpEndorsement,
                        ClassificationConfig.dateTimeFormatter().format(this.toBeReviewedOnTime));
            } else if (pnpEndorsement == PolicyAndPrivacyEndorsements.DEPARTMENT_USE_ONLY) {
                marking = new PolicyAndPrivacyEndorsementMarking(pnpEndorsement,
                        formatDepartments(this.departmentUseOnlyDepartments));
            } else {
                marking = new PolicyAndPrivacyEndorsementMarking(pnpEndorsement, null);
            }
            pnpEndorsementList.add(marking);
        }

        NationalSecurityEndorsements ncEndorsements = null;
        if (hasNationalSecurityEndorsements()) {
            ReleasabilityMarking releasability = null;
            if (this.releasablityType != null) {
                releasability = new ReleasabilityMarking(this.releasablityType, List.copyOf(this.releasableToList));
            }
            
            ncEndorsements = new NationalSecurityEndorsements(this.accountableMaterial,
                    List.copyOf(this.sensitiveCompartments), List.copyOf(this.disseminationMarks), releasability);
        }
        

        return new ProtectiveMarker(this.classification, pnpEndorsementList, ncEndorsements);
    }

    private void checkClassificationValid(List<String> report) {
        if (this.classification == null) {
            report.add("Classification must be set.");
        }
        if (Classification.topSecret().equals(this.classification) && !this.accountableMaterial) {
            report.add("For classification '" + Classification.topSecret().toString()
                    + "' ACCOUNTABLE MATERIAL must be true.");
        }
    }

    private void checkPolicyAndPrivacyEndorementsValid(List<String> report) {
        if (this.classification != null && !Utils.policyAndPrivacyClassifications().contains(this.classification) && hasPolicyAndPrivacyEndorsements()) {
            report.add(
                    "May only have Policy And Privacy Endorsements for Policy And Privacy Classifications. They are not permitted on: "
                            + this.classification.toString());
        }
        if (this.policyAndPrivacyEndorsements.contains(PolicyAndPrivacyEndorsements.EMBARGOED_FOR_RELEASE)
                && this.embargoedForReleaseTime == null) {
            report.add("For Endorsement '" + PolicyAndPrivacyEndorsements.EMBARGOED_FOR_RELEASE.toString()
                    + "', an Embargo Date Time must be set.");
        }
        if (this.policyAndPrivacyEndorsements.contains(PolicyAndPrivacyEndorsements.TO_BE_REVIEWED_ON)
                && this.toBeReviewedOnTime == null) {
            report.add("For Endorsement '" + PolicyAndPrivacyEndorsements.TO_BE_REVIEWED_ON.toString()
                    + "', a Review Date Time must be set.");
        }
        if (this.policyAndPrivacyEndorsements.contains(PolicyAndPrivacyEndorsements.DEPARTMENT_USE_ONLY)
                && this.departmentUseOnlyDepartments.isEmpty()) {
            report.add("For Endorsement '" + PolicyAndPrivacyEndorsements.DEPARTMENT_USE_ONLY.toString()
                    + "', one or more Departments are required.");
        }

        if (this.embargoedForReleaseTime != null
                && !this.policyAndPrivacyEndorsements.contains(PolicyAndPrivacyEndorsements.EMBARGOED_FOR_RELEASE)) {
            report.add("If an Embargoed For Release Date Time is set, an '"
                    + PolicyAndPrivacyEndorsements.EMBARGOED_FOR_RELEASE.toString() + "' Endorsement is required.");
        }
        if (this.toBeReviewedOnTime != null
                && !this.policyAndPrivacyEndorsements.contains(PolicyAndPrivacyEndorsements.TO_BE_REVIEWED_ON)) {
            report.add("If a To Be Reviewed Date Time is set, a '"
                    + PolicyAndPrivacyEndorsements.TO_BE_REVIEWED_ON.toString() + "' Endorsement is required.");
        }
        if (!this.departmentUseOnlyDepartments.isEmpty()
                && !this.policyAndPrivacyEndorsements.contains(PolicyAndPrivacyEndorsements.DEPARTMENT_USE_ONLY)) {
            report.add("If a List of Use Only Departments is set, a '"
                    + PolicyAndPrivacyEndorsements.DEPARTMENT_USE_ONLY.toString() + "' Endorsement is required.");
        }
    }

    private void checkSecurityEndorsements(List<String> report) {
        if (hasNationalSecurityEndorsements()) {
            if (this.classification != null && !Utils.nationalSecurityClassifications().contains(this.classification) && hasNationalSecurityEndorsements()) {
                report.add(
                        "May only have National Security Endorsements for National Security Classifications. They are not permitted on: "
                                + this.classification.toString());
            }
            if (this.releasablityType == ReleasabilityTypes.RELTO) {
                // make sure releasable to list starts with "NZL" and has a length of at least
                // 2.
                if (!this.releasableToList.contains(Utils.NZL)) {
                    report.add("Releasable To Lists must contain `NZL`");
                }
                if (this.releasableToList.size() < 2) {
                    report.add("Releasable To List must have a minimum size of 2");
                }
            } else {
                // make sure releasable to list is empty.
                if (!this.releasableToList.isEmpty()) {
                    report.add("Cannot have a releasable to list with Releasability Type: " + this.releasablityType);
                }
            }
        }
    }

    private String formatDepartments(Set<String> departmentSet) {
        StringBuilder buf = new StringBuilder();
        for (String department : departmentSet) {
            if (buf.length() > 0) {
                buf.append(", ");
            }
            buf.append(department);
        }
        return buf.toString();
    }

    private Set<String> parseDepartments(String departments) {
        return Set.of(departments.split(", "));
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountableMaterial, classification, departmentUseOnlyDepartments, disseminationMarks,
                embargoedForReleaseTime, policyAndPrivacyEndorsements, releasableToList, releasablityType,
                sensitiveCompartments, toBeReviewedOnTime);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ProtectiveMarkerBuilder other = (ProtectiveMarkerBuilder) obj;
        return accountableMaterial == other.accountableMaterial && Objects.equals(classification, other.classification)
                && Objects.equals(departmentUseOnlyDepartments, other.departmentUseOnlyDepartments)
                && Objects.equals(disseminationMarks, other.disseminationMarks)
                && Objects.equals(embargoedForReleaseTime, other.embargoedForReleaseTime)
                && Objects.equals(policyAndPrivacyEndorsements, other.policyAndPrivacyEndorsements)
                && Objects.equals(releasableToList, other.releasableToList)
                && releasablityType == other.releasablityType
                && Objects.equals(sensitiveCompartments, other.sensitiveCompartments)
                && Objects.equals(toBeReviewedOnTime, other.toBeReviewedOnTime);
    }

}
