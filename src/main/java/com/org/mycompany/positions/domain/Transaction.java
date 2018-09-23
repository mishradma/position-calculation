/**
 * 
 */
package com.org.mycompany.positions.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.org.mycompany.positions.PositionConstant;

/**
 * @author Dayanand Mishra
 *
 */
@Entity
@Table(name = "T_DAILY_TRANSACTION")
@AttributeOverride(name = "uniqueDBID", column = @Column(unique = true, name = "TRANSACTION_ID", nullable = false))
public class Transaction extends PersistableEntity implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -6037911238020169507L;
	@JsonAlias("Instrument")
	private String instrument;
	@JsonAlias("TransactionType")
	private String transactionType;
	@JsonAlias("TransactionQuantity")
	private Long transactionQuantity;

	private String txnDate;

	/**
	 *
	 */
	public Transaction() {
	}

	/**
	 * @param instrument
	 * @param transactionType
	 * @param transactionQuantity
	 */
	public Transaction(String instrument, String transactionType, Long transactionQuantity) {
		super();
		this.instrument = instrument;
		this.transactionType = transactionType;
		this.transactionQuantity = transactionQuantity;
	}

	/**
	 * @return the transactionId
	 */
	@Transient
	@JsonAlias("TransactionId")
	public long getTransactionId() {
		return this.getUniqueDBID();
	}

	/**
	 * @param transactionId
	 *            the producId to set
	 */
	@Transient
	public void setTransactionId(final long transactionId) {
		this.setUniqueDBID(transactionId);
	}

	/**
	 * @return the instrument
	 */
	public String getInstrument() {
		return instrument;
	}

	/**
	 * @param instrument
	 *            the instrument to set
	 */
	public void setInstrument(String instrument) {
		this.instrument = instrument;
	}

	/**
	 * @return the transactionType
	 */
	public String getTransactionType() {
		return transactionType;
	}

	/**
	 * @param transactionType
	 *            the transactionType to set
	 */
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	/**
	 * @return the transactionQuantity
	 */
	public Long getTransactionQuantity() {
		return transactionQuantity;
	}

	/**
	 * @param transactionQuantity
	 *            the transactionQuantity to set
	 */
	public void setTransactionQuantity(Long transactionQuantity) {
		this.transactionQuantity = transactionQuantity;
	}

	/**
	 * @return the txnDate
	 */
	public String getTxnDate() {
		return txnDate = ObjectUtils.defaultIfNull(txnDate,
				DateFormatUtils.format(new Date(), PositionConstant.DEFAULT_DATE_FORMAT));
	}

	/**
	 * @param txnDate
	 *            the txnDate to set
	 */
	public void setTxnDate(String txnDate) {
		this.txnDate = txnDate;
	}

	@Override
	public String toString() {
		return StringUtils.join(getTransactionId(), ",", getInstrument(), ",", getTransactionType(), ",",
				getTransactionQuantity(), ",", getTxnDate());
	}
}
