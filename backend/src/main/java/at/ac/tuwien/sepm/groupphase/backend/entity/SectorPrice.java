package at.ac.tuwien.sepm.groupphase.backend.entity;

import at.ac.tuwien.sepm.groupphase.backend.entity.embeddables.SectorPriceId;
import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

@Entity
public class SectorPrice {
    @EmbeddedId
    SectorPriceId id = new SectorPriceId();

    @ManyToOne
    @MapsId("sectorId")
    @JoinColumn(name = "sectorId")
    private Sector sector;

    @ManyToOne
    @MapsId("showId")
    @JoinColumn(name = "showId")
    private Show show;

    @Column(nullable = false)
    private BigDecimal price;

    public SectorPrice() {
    }

    public SectorPrice(Sector sector, Show show, BigDecimal price) {
        this.sector = sector;
        this.show = show;
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SectorPrice that = (SectorPrice) o;
        return Objects.equals(sector, that.sector) && Objects.equals(show, that.show)
            && Objects.equals(price, that.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sector, show, price);
    }

    @Override
    public String toString() {
        return "SectorPrice{"
            + "sector=" + sector
            + ", show=" + show
            + ", price=" + price
            + '}';
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public SectorPriceId getId() {
        return id;
    }

    public void setId(SectorPriceId id) {
        this.id = id;
    }

    public Sector getSector() {
        return sector;
    }

    public void setSector(Sector sector) {
        this.sector = sector;
    }

    public Show getShow() {
        return show;
    }

    public void setShow(Show show) {
        this.show = show;
    }
}
