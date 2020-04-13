package com.brownbag_api.service;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brownbag_api.model.Asset;
import com.brownbag_api.model.BalSheet;
import com.brownbag_api.model.Log;
import com.brownbag_api.model.OrderPay;
import com.brownbag_api.model.Pos;
import com.brownbag_api.model.User;
import com.brownbag_api.model.data.EAsset;
import com.brownbag_api.model.data.EBookType;
import com.brownbag_api.model.data.EOrderAction;
import com.brownbag_api.model.data.EOrderStatus;
import com.brownbag_api.model.data.EOrderType;
import com.brownbag_api.repo.AssetRepo;
import com.brownbag_api.repo.LogRepo;
import com.brownbag_api.repo.OrderPayRepo;
import com.brownbag_api.repo.OrderRepo;
import com.brownbag_api.util.UtilDate;

@Service
public class LogSvc extends OrderSvc {

	@Autowired
	private LogRepo logRepo;

	public void write(String msg) {

		Log log = new Log(msg);
		logRepo.save(log);
	}

	public List<Log> getAll() {
		return logRepo.findAll();
	}

}
