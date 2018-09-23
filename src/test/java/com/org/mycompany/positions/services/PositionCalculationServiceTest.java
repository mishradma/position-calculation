package com.org.mycompany.positions.services;

import java.util.List;
import java.util.Objects;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.google.common.collect.Lists;
import com.org.mycompany.positions.PositionConstant;
import com.org.mycompany.positions.domain.Position;
import com.org.mycompany.positions.domain.Transaction;
import com.org.mycompany.positions.repository.PositionRepository;
import com.org.mycompany.positions.repository.TransactionRepository;

@RunWith(MockitoJUnitRunner.class)
public class PositionCalculationServiceTest {
	@InjectMocks
	private PositionCalculationService underTest = new PositionCalculationService();

	@Mock
	PositionRepository positionRepositoryMock;
	@Mock
	TransactionRepository transactionRepositoryMock;
	List<Position> sodPosition;
	List<Transaction> transactions;
	List<Transaction> groupTransaction;;

	@Before
	public void setUp() {
		sodPosition = Lists.newArrayList();
		transactions = Lists.newArrayList();
		groupTransaction = Lists.newArrayList();
		sodPosition.add(new Position("APPL", "101", "E", 10000L));
		sodPosition.add(new Position("APPL", "201", "I", -10000L));

		transactions.add(new Transaction("APPL", "B", 100L));
		transactions.add(new Transaction("APPL", "S", 20000L));
		transactions.add(new Transaction("APPL", "S", 200L));
		transactions.add(new Transaction("APPL", "B", 9000L));
		groupTransaction.add(new Transaction("APPL", "S", 20200L));
		groupTransaction.add(new Transaction("APPL", "B", 9100L));
	}

	/**
	 * 
	 * <pre>
	 * =========================================================================================================================
	############################################Each Transaction wise Calculation############################################
	=========================================================================================================================
	BUY =   -------------------------
		|Tx_ID		|	Tx_Qty	|
		-------------------------
		| 5			| 100		|
		-------------------------
		| 10		| 9000		|
		-------------------------
		
	SELL =  -------------------------
		|Tx_ID		|	Tx_Qty	|
		-------------------------
		| 1			| 200		|
		-------------------------
		| 6			| 20000		|
		-------------------------
	
	Start of the Day Position Entry:-		
	APPL,101,E,10000
	APPL,201,I,-10000
	
	E, Quantity(I/P Position file) =  10000
	I, Quantity(I/P Position file) = -10000
	=========================================================================================================================
	If Transaction Type = B ,
	                            For AccountType = E, Quantity = Quantity + TransactionQuantity
	                            For AccountType = I, Quantity = Quantity - TransactionQuantity
	If Transaction Type = S ,
	                            For AccountType = E, Quantity = Quantity - TransactionQuantity
	                            For AccountType = I, Quantity = Quantity + TransactionQuantity                                                                               
	=========================================================================================================================                                                              TransactionID 1) SELL
	E, Quantity = Quantity(I/P Position file) - TransactionQuantity
	I, Quantity = Quantity(I/P Position file) + TransactionQuantity
	
	E =  10000 - 200 =  9800
	I = -10000 + 200 = -9800
	---------------------------------------------------
	TransactionID 5) BUY
	E, Quantity = Quantity(Position from previous Txn) + TransactionQuantity
	I, Quantity = Quantity(Position from previous Txn) - TransactionQuantity
	
	E =  9800 + 100 =  9900
	I = -9800 - 100 = -9900
	---------------------------------------------------
	TransactionID 6) SELL
	E, Quantity = Quantity(Position from previous Txn) - TransactionQuantity
	I, Quantity = Quantity(Position from previous Txn) + TransactionQuantity
	
	E =  9900 - 20000 = -10100
	I = -9900 + 20000 =  10100
	---------------------------------------------------
	TransactionID 10) BUY  
	E, Quantity = Quantity(Position from previous Txn) + TransactionQuantity
	I, Quantity = Quantity(Position from previous Txn) - TransactionQuantity
	                                                                                                             
	E = -10100 + 9000 =  -1100   	End of the Day Position on Acc "E" after applying all the TransactionQuantity
	I =  10100 - 9000 =   1100   	End of the Day Position on Acc "I" after applying all the TransactionQuantity 
	=========================================================================================================================                                                              Delta
	E = -1100 - 10000   = -11100 	Net VOlume
	I =  1100 -(-10000) =  11100 	Net VOlume
	=========================================================================================================================
	Expected End of the Day Position:- 
	APPL,101,I,-1100,-11100
	APPL,201,E,1100,11100
	
	Calculated End of the Day Position:-
	APPL,101,I,1100,11100
	APPL,201,E,-1100,-11100
	=========================================================================================================================
	 * </pre>
	 */
	@Test
	public void testAdjustEndOfTheDayaPosition() {
		Mockito.when(transactionRepositoryMock.findAll()).thenReturn(transactions);
		Mockito.when(transactionRepositoryMock.findTransactionByDateAndTransactionType()).thenReturn(groupTransaction);
		Mockito.when(positionRepositoryMock.findAll()).thenReturn(sodPosition);

		underTest.adjustEndOfTheDayaPosition();

		Mockito.verify(transactionRepositoryMock, Mockito.atLeastOnce()).findAll();
		Mockito.verify(transactionRepositoryMock, Mockito.atLeastOnce()).findTransactionByDateAndTransactionType();
		Mockito.verify(positionRepositoryMock, Mockito.atLeastOnce()).findAll();

		Position position = CollectionUtils.find(sodPosition,
				item -> StringUtils.equalsIgnoreCase(item.getAccountType(), PositionConstant.EXTERNAL));
		Assert.assertTrue(Objects.equals(-1100L, position.getEodQuantity()));

		position = CollectionUtils.find(sodPosition,
				item -> StringUtils.equalsIgnoreCase(item.getAccountType(), PositionConstant.INTERNAL));
		Assert.assertTrue(Objects.equals(1100L, position.getEodQuantity()));
	}

}
