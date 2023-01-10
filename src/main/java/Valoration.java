import java.util.Objects;

public class Valoration {
    String category;
    double score;

    public Valoration(String category, double score) {
        this.category = category;
        this.score = score;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Valoration that = (Valoration) o;
        return Double.compare(that.score, score) == 0 && Objects.equals(category, that.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(category, score);
    }

    @Override
    public String toString() {
        return "Valoration{" +
                "category='" + category + '\'' +
                ", score=" + score +
                '}';
    }
}
