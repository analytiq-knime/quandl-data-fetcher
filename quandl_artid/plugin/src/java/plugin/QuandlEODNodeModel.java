package org.eod;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import org.json.JSONArray;
import org.json.JSONObject;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.RowKey;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

/**
 * This is the model implementation of QuandlEOD.
 * 
 *
 * @author I Tang Lo
 */
public class QuandlEODNodeModel extends NodeModel {

	// the logger instance
	private static final NodeLogger logger = NodeLogger.getLogger(QuandlEODNodeModel.class);

	public static QuandlEODConfig m_config = new QuandlEODConfig();

	public static ArrayList<String> m_tickers = new ArrayList<String>();

	protected QuandlEODNodeModel() {

		super(0, 1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData, final ExecutionContext exec)
			throws Exception {

		if (inData != null && inData.length != 0)
			throw new IllegalArgumentException("bad input");

		BufferedDataContainer container = null;

		if (m_tickers.size() == 0) {

			JOptionPane.showMessageDialog(null, "Please pick the ticker !! \n Ex: AAPL", "Error",
					JOptionPane.ERROR_MESSAGE);

			return new BufferedDataTable[] { null };

		} else {

			System.out.println("m_tickers.size() = " + m_tickers.size());
			System.out
					.println("m_tickers.getm_api_key() = " + m_config.getm_api_key() + "m_tickers.getM_start_date() = "
							+ m_config.getM_start_date() + "m_tickers.getM_end_date() = " + m_config.getM_end_date());

			container = exec.createDataContainer(createEODDataColumnSpec());

			String message = "";

			Long count = 0L;

			for (String ticker : m_tickers) {

				String urlStr = "https://www.quandl.com/api/v3/datasets/EOD/" + ticker + ".json?"
						+ ("".equals(m_config.getM_start_date()) ? ""
								: "start_date=" + m_config.getM_start_date() + "&")
						+ ("".equals(m_config.getM_end_date()) ? "" : "end_date=" + m_config.getM_end_date() + "&")
						+ "api_key=" + m_config.getm_api_key();
				System.out.println("urlStr = " + urlStr);

				JSONObject searchJSONObj = getJSONObjectFromURL(urlStr);

				if (searchJSONObj != null) {

					// quandl return error
					try {

						JSONObject errorJSONObj = searchJSONObj.getJSONObject("quandl_error");
						message += "ticker : " + ticker + "\n";
						message += "error code : " + errorJSONObj.getJSONObject("code").toString() + "\n";
						message += "message : " + errorJSONObj.getJSONObject("message").toString() + "\n\n";

					} catch (Exception e) {

						JSONObject dataset = searchJSONObj.getJSONObject("dataset");

						JSONArray columnNames = dataset.getJSONArray("column_names");

						List<Object> columnNamesAry = columnNames.toList();

						JSONArray data = (JSONArray) dataset.get("data"); // data:[[..] , [..]]

						List<Object> dataAry = data.toList();

						for (int i = 0; i < dataAry.size(); i++) {

							DataCell[] cells = new DataCell[14];

							List<Object> rowData = (List<Object>) dataAry.get(i);

							for (int j = 0; j <= 13; j++) {

								if (j == 0) {
									cells[j] = new StringCell(ticker);
								} else if (j == 1) {
									cells[j] = new StringCell((String) rowData.get(j - 1));
								} else
									cells[j] = new DoubleCell((Double) rowData.get(j - 1));

							}

							container.addRowToTable(new DefaultRow(RowKey.createRowKey(count), cells));

							count++;
						}

					}
				}

			}

			container.close();

			BufferedDataTable result = container.getTable();

			container = null;

			if (!"".equals(message))
				logger.error("Fetch Fail Result: \n" + message);
//			JOptionPane.showMessageDialog(null, "Reason: \n" + message, "Missing Data",
//					JOptionPane.WARNING_MESSAGE);
			System.out.println(message);

			return new BufferedDataTable[] { result };

		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void reset() {

		System.out.println("Enter into reset()");
		//m_config = null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {

		System.out.println("QuandlEODNodeModel: Enter into saveSettingsTo()");
		if (m_config != null) {
			m_config.saveSettingsTo(settings);
			System.out.println("m_tickers.size() = " + m_tickers.size());
			System.out
					.println("m_tickers.getm_api_key() = " + m_config.getm_api_key() + "m_tickers.getM_start_date() = "
							+ m_config.getM_start_date() + "m_tickers.getM_end_date() = " + m_config.getM_end_date());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {

		System.out.println("QuandlEODNodeModel: Enter into loadValidatedSettingsFrom()");
		
		QuandlEODConfig config = new QuandlEODConfig();
		config.loadSettingsInModel(settings);
		m_config=config;
	//	QuandlEODNodeModel.m_config.loadSettingsInModel(settings);
		System.out.println("m_tickers.size() = " + m_tickers.size());
		System.out.println("m_tickers.getm_api_key() = " + m_config.getm_api_key() + "m_tickers.getM_start_date() = "
				+ m_config.getM_start_date() + "m_tickers.getM_end_date() = " + m_config.getM_end_date());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {

		System.out.println("QuandlEODNodeModel: Enter into validateSettings()");
//		if (QuandlEODNodeModel.m_config != null)
//			QuandlEODNodeModel.m_config.loadSettingsInModel(settings);
		new QuandlEODConfig().loadSettingsInModel(settings);
		System.out.println("m_tickers.size() = " + m_tickers.size());
		System.out.println("m_tickers.getm_api_key() = " + m_config.getm_api_key() + "m_tickers.getM_start_date() = "
				+ m_config.getM_start_date() + "m_tickers.getM_end_date() = " + m_config.getM_end_date());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected DataTableSpec[] configure(final DataTableSpec[] inSpecs) throws InvalidSettingsException {

		if (m_config == null) {
			throw new InvalidSettingsException("QuandlEODNodeModel: No settings available");
		}

		return new DataTableSpec[] { createEODDataColumnSpec() };
	}

	private DataTableSpec createEODDataColumnSpec() {

		DataColumnSpec[] allColSpecs = new DataColumnSpec[14];

		allColSpecs[0] = new DataColumnSpecCreator("Ticker_Name", StringCell.TYPE).createSpec();
		allColSpecs[1] = new DataColumnSpecCreator("Date", StringCell.TYPE).createSpec();
		allColSpecs[2] = new DataColumnSpecCreator("Open", DoubleCell.TYPE).createSpec();
		allColSpecs[3] = new DataColumnSpecCreator("High", DoubleCell.TYPE).createSpec();
		allColSpecs[4] = new DataColumnSpecCreator("Low", DoubleCell.TYPE).createSpec();
		allColSpecs[5] = new DataColumnSpecCreator("Close", DoubleCell.TYPE).createSpec();
		allColSpecs[6] = new DataColumnSpecCreator("Volume", DoubleCell.TYPE).createSpec();
		allColSpecs[7] = new DataColumnSpecCreator("Dividend", DoubleCell.TYPE).createSpec();
		allColSpecs[8] = new DataColumnSpecCreator("Split", DoubleCell.TYPE).createSpec();
		allColSpecs[9] = new DataColumnSpecCreator("Adj_Open", DoubleCell.TYPE).createSpec();
		allColSpecs[10] = new DataColumnSpecCreator("Adj_High", DoubleCell.TYPE).createSpec();
		allColSpecs[11] = new DataColumnSpecCreator("Adj_Low", DoubleCell.TYPE).createSpec();
		allColSpecs[12] = new DataColumnSpecCreator("Adj_Close", DoubleCell.TYPE).createSpec();
		allColSpecs[13] = new DataColumnSpecCreator("Adj_Volume", DoubleCell.TYPE).createSpec();
		return new DataTableSpec(allColSpecs);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadInternals(final File internDir, final ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {

		System.out.println("QuandlEODNodeModel: Enter into loadInternals()");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveInternals(final File internDir, final ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {

		System.out.println("QuandlEODNodeModel: Enter into saveInternals()");

	}

	public static JSONObject getJSONObjectFromURL(String urlStr) {

		try {
			URL url = new URL(urlStr);
			URLConnection urlConnection = url.openConnection();
			HttpURLConnection connection = null;
			if (urlConnection instanceof HttpURLConnection) {
				connection = (HttpURLConnection) urlConnection;
			} else {
				System.out.println("QuandlEODNodeModel: No connection");
				return null;
			}
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			StringBuilder urlString = new StringBuilder();
			String current;
			while ((current = in.readLine()) != null) {
				urlString.append(current);
			}

			JSONObject json = new JSONObject(urlString.toString());

			return json;

		} catch (IOException e1) {
			e1.toString();
		}
		return null;
	}

}
