package com.univas.teste_integracao.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Store {

	private double amountStored;

	private double minimumAmount;

	private String provider;

	private String location;

	private String joinDate;

	private boolean active;

}
