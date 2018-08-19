package com.licj.viewworldweb.recommender;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.model.DataModel;

public class ItemFileDataModel implements ItemDataModel{
	private static final Logger LOGGER = Logger.getLogger(ItemFileDataModel.class);
	
	public File modelFile = null;
	
	public ItemFileDataModel(){
		modelFile = new File("D:/12-licj/eclipse-workspace/ViewWorldWeb/src/main/java/resource/neteasy_rates.csv");
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
