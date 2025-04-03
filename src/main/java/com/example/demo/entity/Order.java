package com.example.demo.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "orders")
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id; // 注文ID

	@Column(name = "customer_id")
	private Integer customerId; // 顧客ID

	@Column(name = "ordered_on")
	private LocalDate orderedOn; // 注文日

	@Column(name = "total_price")
	private Integer totalPrice; // 合計金額

	// コンストラクタ
	public Order() {
	}

	public Order(Integer customerId, LocalDate orderedOn, Integer totalPrice) {
		this.customerId = customerId;
		this.orderedOn = orderedOn;
		this.totalPrice = totalPrice;
	}

	// ゲッター
	public Integer getId() {
		return id;
	}
}
