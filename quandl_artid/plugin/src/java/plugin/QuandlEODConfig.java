package org.eod;

import java.util.ArrayList;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

public class QuandlEODConfig {

	private String m_api_key; // mdJ3-9txxdtZdZRH4HXJ

	private String m_start_date;

	private String m_end_date;
	
	private String m_tickers;


	public QuandlEODConfig() {

		m_api_key = "mdJ3-9txxdtZdZRH4HXJ"; //mdJ3-9txxdtZdZRH4HXJ
		m_start_date = "";
		m_end_date = "";
	    m_tickers = "";
	}

	public void loadSettingsInDialog(final NodeSettingsRO settings) {

		m_api_key = settings.getString("api_key", m_api_key);
		m_start_date = settings.getString("start_date", m_start_date);
		m_end_date = settings.getString("end_date", m_end_date);
		m_tickers = settings.getString("tickers", m_tickers);
	}

	public void loadSettingsInModel(final NodeSettingsRO settings) throws InvalidSettingsException {
        
		m_api_key = settings.getString("api_key", m_api_key);
		m_start_date = settings.getString("start_date", m_start_date);
		m_end_date = settings.getString("end_date", m_end_date);
		m_tickers = settings.getString("tickers", m_tickers);
	}

	public void saveSettingsTo(final NodeSettingsWO settings) {
		
		settings.addString("api_key", m_api_key);
		settings.addString("start_date", m_start_date);
		settings.addString("end_date", m_end_date);
		settings.addString("tickers", m_tickers);
	}
	

	public String getm_api_key() {
		return m_api_key;
	}

	public void setm_api_key(String m_api_key) {
		this.m_api_key = m_api_key;
	}

	public String getM_start_date() {
		return m_start_date;
	}

	public void setM_start_date(String m_start_date) {
		this.m_start_date = m_start_date;
	}

	public String getM_end_date() {
		return m_end_date;
	}

	public void setM_end_date(String m_end_date) {
		this.m_end_date = m_end_date;
	}

	public String getM_tickers() {
		return m_tickers;
	}

	public void setM_tickers(String m_tickers) {
		this.m_tickers = m_tickers;
	}

}
