package com.weatherObserver.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "weatherCondition")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeatherCondition implements Serializable{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String cityKey;
	@OneToOne(cascade=CascadeType.PERSIST)
	private Temperature temperature;
	private LocalDateTime localObservationDateTime;
}
