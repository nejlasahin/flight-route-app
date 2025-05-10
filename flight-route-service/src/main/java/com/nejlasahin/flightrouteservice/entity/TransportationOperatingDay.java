package com.nejlasahin.flightrouteservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "transportation_operating_days")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransportationOperatingDay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "operating_day", nullable = false)
    private Integer operatingDay;

    @ManyToOne
    @JoinColumn(name = "transportation_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Transportation transportation;

    public TransportationOperatingDay(Integer operatingDay, Transportation transportation) {
        this.operatingDay = operatingDay;
        this.transportation = transportation;
    }
}