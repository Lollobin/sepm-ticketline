package at.ac.tuwien.sepm.groupphase.backend.entity;

import at.ac.tuwien.sepm.groupphase.backend.entity.embeddables.SectorPriceId;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class SectorPrice {
    @EmbeddedId
    SectorPriceId id;

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
}
