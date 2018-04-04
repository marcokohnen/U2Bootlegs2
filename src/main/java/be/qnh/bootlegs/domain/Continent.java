package be.qnh.bootlegs.domain;

public enum Continent {

    AFRICA("africa"), ASIA("asia"), AUSTRALIA("australia"), EUROPE("europe"), NORTHAMERICA("northamerica"), OCEANIE("oceanië"), SOUTHAMERICA("southamerica");

    private final String stringValue;

    Continent(String stringValue) {
        this.stringValue = stringValue;
    }

    public String getStringValue() {
        return stringValue.toLowerCase();
    }


}
