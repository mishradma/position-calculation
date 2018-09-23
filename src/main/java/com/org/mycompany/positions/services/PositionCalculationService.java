/**
 * 
 */
package com.org.mycompany.positions.services;

import java.util.Collection;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.org.mycompany.positions.PositionConstant;
import com.org.mycompany.positions.domain.Position;
import com.org.mycompany.positions.domain.Transaction;
import com.org.mycompany.positions.repository.PositionRepository;
import com.org.mycompany.positions.repository.TransactionRepository;

/**
 * 
 * This is a position calculation service used to calculate end of day position.
 * Currently it is assumed Start of day position will come only once when
 * application is up and running.
 * 
 * @author Dayanand Mishra
 *
 */
@Component
public class PositionCalculationService {

	@Autowired
	private PositionRepository positionRepository;

	@Autowired
	private TransactionRepository transactionRepository;

	public void adjustEndOfTheDayaPosition() {
		Iterable<Position> startOfDayPosition = positionRepository.findAll();
		Collection<Transaction> dailyTransactions = transactionRepository.findTransactionByDateAndTransactionType();
		// This just to print saved content
		printSummaryOfInputData(dailyTransactions, startOfDayPosition);
		startOfDayPosition.forEach(position -> {
			dailyTransactions.forEach((transaction) -> {
				if (StringUtils.equalsIgnoreCase(transaction.getInstrument(), position.getInstrument())) {
					if (StringUtils.equalsIgnoreCase(PositionConstant.BUY, transaction.getTransactionType())) {
						if (StringUtils.equalsIgnoreCase(PositionConstant.EXTERNAL, position.getAccountType())) {
							// Qty calculated from BUY Txn
							position.setEodQuantity(position.getEodQuantity() + transaction.getTransactionQuantity());
						} else if (StringUtils.equalsIgnoreCase(PositionConstant.INTERNAL, position.getAccountType())) {
							// Qty calculated from BUY Txn
							position.setEodQuantity(position.getEodQuantity() - transaction.getTransactionQuantity());
						}
					} else {
						if (StringUtils.equalsIgnoreCase(PositionConstant.EXTERNAL, position.getAccountType())) {
							// Qty calculated from BUY Txn
							position.setEodQuantity(position.getEodQuantity() - transaction.getTransactionQuantity());
						} else if (StringUtils.equalsIgnoreCase(PositionConstant.INTERNAL, position.getAccountType())) {
							// Qty calculated from BUY Txn
							position.setEodQuantity(position.getEodQuantity() + transaction.getTransactionQuantity());
						}
					}
				}
			});
		});
		// Printing to console for POC purpose only .Sysout Should not be used in actual
		startOfDayPosition.forEach(action -> action.printCurrentPosition());
		// positionRepository.saveAll(startOfDayPosition);

	}

	private void printSummaryOfInputData(Collection<Transaction> dailyTransactions,
			Iterable<Position> startOfDayPosition) {
		System.out.println("************************Start of Day position*********************");
		startOfDayPosition.forEach(action -> System.out.println(action));
		System.out.println("************************Current Actual Transaction*********************");
		transactionRepository.findAll().forEach(action -> System.out.println(action));
		System.out.println("************************Current Grouped Transaction*********************");
		dailyTransactions.forEach(action -> System.out.println(action));
		System.out.println("************************End Of Day position*********************");
	}

}
