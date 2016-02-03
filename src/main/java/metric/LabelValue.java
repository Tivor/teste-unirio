package metric;

public class LabelValue implements Comparable<LabelValue> {

    private String label;
    private Double value;

    public LabelValue(String label, double value) {
        this.label = label;
        this.value = Double.valueOf(value);
    }

    @Override
    public int compareTo(LabelValue o) {
        return o.value.compareTo(this.value);
    }

    @Override
    public boolean equals(Object obj) {
        return this.label.equals(((LabelValue)obj).label);
    }
}