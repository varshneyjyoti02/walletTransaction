package com.capgemini.service;

import java.math.BigDecimal;
import java.util.ArrayList;

import com.capgemini.beans.Customer;
import com.capgemini.beans.Transaction;
import com.capgemini.beans.Wallet;
import com.capgemini.exception.DuplicatePhoneNo;
import com.capgemini.exception.PhoneNoDoesNotExist;
import com.capgemini.repository.WalletRepo;
import com.capgemini.repository.WalletRepoImpl;


public class WalletServiceImpl implements WalletService {
	
	WalletRepo walletRepo = new WalletRepoImpl();
	ArrayList<Transaction> AL = new ArrayList<Transaction>();
	
	@Override
	public Customer createAccount(String name, String mobileNumber, BigDecimal amount) throws DuplicatePhoneNo, PhoneNoDoesNotExist {
		
//		if(walletRepo.findOne(mobileNumber)!=null)
//			throw new DuplicatePhoneNo();
		
		Customer customer=new Customer(name, mobileNumber, new Wallet(amount));
		
//		if(mobileNumber.length()!=10)
//		{
//			System.out.println("Not a valid No");
//		}
//		
//		if(Pattern.compile( "[0-9]" ).matcher( name ).find())
//		{
//			System.out.println("Not a valid Name");
//		}
//		
		if(walletRepo.findOne(mobileNumber)!=null)
		{
			throw new DuplicatePhoneNo();
		}
		
		walletRepo.save(customer);
		return customer;
	}

	@Override
	public Customer showBalance(String mobileNumber) throws PhoneNoDoesNotExist
	{
		if(walletRepo.findOne(mobileNumber)!=null)
			return walletRepo.findOne(mobileNumber);
		else
			throw new PhoneNoDoesNotExist();
	}

	@Override
	public Customer fundTransfer(String sourceMobileNumber, String targetMobileNumber, BigDecimal amount) throws PhoneNoDoesNotExist {
		if(walletRepo.findOne(sourceMobileNumber)==null)
		{
			throw new PhoneNoDoesNotExist();
		}
		if(walletRepo.findOne(targetMobileNumber)==null)
		{
			throw new PhoneNoDoesNotExist();
		}
		this.depositAmount(targetMobileNumber, amount);
		this.withdrawAmount(sourceMobileNumber, amount);
		return walletRepo.findOne(sourceMobileNumber);
	}

	@Override
	public Customer depositAmount(String mobileNumber, BigDecimal amount) throws PhoneNoDoesNotExist
	{		
		if(walletRepo.findOne(mobileNumber)==null)
		{
			throw new PhoneNoDoesNotExist();
		}
		Transaction t = new Transaction();
		Customer customer=walletRepo.findOne(mobileNumber);
		Wallet wallet=customer.getWallet();
		wallet.setBalance(wallet.getBalance().add(amount));
		customer.setWallet(wallet);
		t.setMobile(mobileNumber);
		t.setWallet(customer.getWallet().getBalance());
		AL.add(t);
		return customer;
	}

	@Override
	public Customer withdrawAmount(String mobileNumber, BigDecimal amount) throws PhoneNoDoesNotExist
	{
		if(walletRepo.findOne(mobileNumber)==null)
		{
			throw new PhoneNoDoesNotExist();
		}
		Transaction t = new Transaction();
		Customer customer=walletRepo.findOne(mobileNumber);
		Wallet wallet=customer.getWallet();
		wallet.setBalance(wallet.getBalance().subtract(amount));
		customer.setWallet(wallet);
		t.setMobile(mobileNumber);
		t.setWallet(customer.getWallet().getBalance());
		AL.add(t);
		return customer;
	}
	public ArrayList<Transaction> transactionHistory(String mobileNumber) throws PhoneNoDoesNotExist
	{
		ArrayList<Transaction> tAL = new ArrayList<Transaction>();
		for(Transaction t : AL)
		{
			if(t.getMobile().equals(mobileNumber))
			{
				tAL.add(t);
			}
		}
		return tAL;
	}

}
