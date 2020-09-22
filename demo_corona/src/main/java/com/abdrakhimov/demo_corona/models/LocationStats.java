package com.abdrakhimov.demo_corona.models;

public class LocationStats {

    private String state;
    private String country;
    private int latestTotal;
    private int diffFromPrevDay;

    public int getDiffFromPrevDay() {
        return diffFromPrevDay;
    }

    public void setDiffFromPrevDay(int diffFromPrevDay) {
        this.diffFromPrevDay = diffFromPrevDay;
    }

    public LocationStats() {
    }

    public String getState() {
        return this.state;
    }

    public String getCountry() {
        return this.country;
    }

    public int getLatestTotal() {
        return this.latestTotal;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setLatestTotal(int latestTotal) {
        this.latestTotal = latestTotal;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof LocationStats)) return false;
        final LocationStats other = (LocationStats) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$state = this.getState();
        final Object other$state = other.getState();
        if (this$state == null ? other$state != null : !this$state.equals(other$state)) return false;
        final Object this$country = this.getCountry();
        final Object other$country = other.getCountry();
        if (this$country == null ? other$country != null : !this$country.equals(other$country)) return false;
        if (this.getLatestTotal() != other.getLatestTotal()) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof LocationStats;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $state = this.getState();
        result = result * PRIME + ($state == null ? 43 : $state.hashCode());
        final Object $country = this.getCountry();
        result = result * PRIME + ($country == null ? 43 : $country.hashCode());
        result = result * PRIME + this.getLatestTotal();
        return result;
    }

    public String toString() {
        return "LocationStats(state=" + this.getState() + ", country=" + this.getCountry() + ", latestTotal=" + this.getLatestTotal() + ")";
    }
}
