package com.licj.viewworldweb.recommender;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.model.DataModel;

public class ItemFileDataModel implements ItemDataModel{
	private static final Logger LOGGER = LoggerFactory.getLogger(ItemFileDataModel.class);
	
	public File modelFile = null;
	
	public ItemFileDataModel(){
//		String dataDir = ItemFileDataModel.class.getClassLoader().getResource("").getPath();
		String dataDir = "D:/AppsData/2/eclipse-workspace/ViewWorldWeb/src/main/java/";
		modelFile = new File(dataDir + "resource/neteasy_rates.csv");
	}
	
	public DataModel getFileDataModel() {
		try {
			return new FileDataModel(modelFile);// 加载数据文件
		} catch (IOException e) {
			LOGGER.error("ItemFileDataModel() error!", e);
		}
		return null;
	}

	@Override
	public DataModel getDataModel() {
		return getFileDataModel();
	}

}
