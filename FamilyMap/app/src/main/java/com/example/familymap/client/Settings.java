package com.example.familymap.client;

public class Settings {
    private static Settings instance;
    private Boolean lifeStoryLinesSwitchStatus;
    private Boolean familyTreeLinesSwitchStatus;
    private Boolean spouseLinesSwitchStatus;
    private Boolean fatherSideSwitchStatus;
    private Boolean motherSideSwitchStatus;
    private Boolean maleEventSwitchStatus;
    private Boolean femaleEventSwitchStatus;

    private Settings() {
        lifeStoryLinesSwitchStatus = true;
        familyTreeLinesSwitchStatus = true;
        spouseLinesSwitchStatus = true;
        fatherSideSwitchStatus = true;
        motherSideSwitchStatus = true;
        maleEventSwitchStatus = true;
        femaleEventSwitchStatus = true;
    }

    // Constructor and getter of instance
    public static Settings getInstance() {
        if (instance == null) {
            instance = new Settings();
        }
        return instance;
    }

    public Boolean getLifeStoryLinesSwitchStatus() {
        return lifeStoryLinesSwitchStatus;
    }

    public void setLifeStoryLinesSwitchStatus(Boolean lifeStoryLinesSwitchStatus) {
        this.lifeStoryLinesSwitchStatus = lifeStoryLinesSwitchStatus;
    }

    public Boolean getFamilyTreeLinesSwitchStatus() {
        return familyTreeLinesSwitchStatus;
    }

    public void setFamilyTreeLinesSwitchStatus(Boolean familyTreeLinesSwitchStatus) {
        this.familyTreeLinesSwitchStatus = familyTreeLinesSwitchStatus;
    }

    public Boolean getSpouseLinesSwitchStatus() {
        return spouseLinesSwitchStatus;
    }

    public void setSpouseLinesSwitchStatus(Boolean spouseLinesSwitchStatus) {
        this.spouseLinesSwitchStatus = spouseLinesSwitchStatus;
    }

    public Boolean getFatherSideSwitchStatus() {
        return fatherSideSwitchStatus;
    }

    public void setFatherSideSwitchStatus(Boolean fatherSideSwitchStatus) {
        this.fatherSideSwitchStatus = fatherSideSwitchStatus;
    }

    public Boolean getMotherSideSwitchStatus() {
        return motherSideSwitchStatus;
    }

    public void setMotherSideSwitchStatus(Boolean motherSideSwitchStatus) {
        this.motherSideSwitchStatus = motherSideSwitchStatus;
    }

    public Boolean getMaleEventSwitchStatus() {
        return maleEventSwitchStatus;
    }

    public void setMaleEventSwitchStatus(Boolean maleEventSwitchStatus) {
        this.maleEventSwitchStatus = maleEventSwitchStatus;
    }

    public Boolean getFemaleEventSwitchStatus() {
        return femaleEventSwitchStatus;
    }

    public void setFemaleEventSwitchStatus(Boolean femaleEventSwitchStatus) {
        this.femaleEventSwitchStatus = femaleEventSwitchStatus;
    }

    public void reset() {
        // Call on logout
        instance = null;
    }
}
