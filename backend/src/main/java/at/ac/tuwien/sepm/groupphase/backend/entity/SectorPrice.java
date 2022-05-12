package at.ac.tuwien.sepm.groupphase.backend.entity;

import at.ac.tuwien.sepm.groupphase.backend.entity.embeddables.SectorPriceId;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

@Entity
public class SectorPrice {
    @EmbeddedId
    SectorPriceId id;

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
