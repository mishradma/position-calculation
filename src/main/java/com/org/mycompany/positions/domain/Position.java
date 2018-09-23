package com.org.mycompany.positions.domain;

import java.util.Date;
import java.util.Objects;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import com.org.mycompany.positions.PositionConstant;

/**
 * @author Dayanand Mishra
 *
 */
@Entity
@Table(name = "T_POSITIONS")
@AttributeOverride(name = "uniqueDBID", column = @Column(unique = true, name = "POS_ID", nullable = false))
public class Position extends PersistableEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2043260598062329277L;
	private String instrument;
	private String account;
	private String accountType;
	private Long quantity;
	private Long eodQuantity;
	private String txnDate;

	/**
	 * @param instrument
	 * @param account
	 * @param accountType
	 * @param quantity
	 */
	public Position(String instrument, String account, String accountType, Long quantity) {
		super();
		this.instrument = instrument;
		this.account = account;
		this.accountType = accountType;
		this.quantity = quantity;
	}

	public Position() {
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
	 * @return the account
	 */
	public String getAccount() {
		return account;
	}

	/**
	 * @param account
	 *            the account to set
	 */
	public void setAccount(String account) {
		this.account = account;
	}

	/**
	 * @return the accountType
	 */
	public String getAccountType() {
		return accountType;
	}

	/**
	 * @param accountType
	 *            the accountType to set
	 */
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	/**
	 * @return the quantity
	 */
	public Long getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity
	 *            the quantity to set
	 */
	public void setQuantity(Long quantity) {
		this.quantity = quantity;
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
		return StringUtils.join(instrument, PositionConstant.COMMA, account, PositionConstant.COMMA, accountType,
				PositionConstant.COMMA, quantity);
	}

	public Long getEodQuantity() {
		return Objects.equals(ObjectUtils.defaultIfNull(eodQuantity, 0L), 0L) ? getQuantity() : eodQuantity;
	}

	public void setEodQuantity(Long eodQuantity) {
		this.eodQuantity = eodQuantity;
	}

	public void printCurrentPosition() {
		System.out.println(StringUtils.join(instrument, PositionConstant.COMMA, account, PositionConstant.COMMA,
				accountType, PositionConstant.COMMA, getEodQuantity(), PositionConstant.COMMA,
				getEodQuantity() - getQuantity()));
	}

}