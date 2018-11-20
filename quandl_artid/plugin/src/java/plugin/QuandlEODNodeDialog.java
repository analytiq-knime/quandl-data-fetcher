package org.eod;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Properties;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import org.json.JSONObject;
import org.knime.core.data.DataTableSpec;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.util.FilesHistoryPanel;
import org.knime.core.node.util.FilesHistoryPanel.LocationValidation;
import org.knime.core.node.workflow.FlowVariable;

/**
 * <code>NodeDialog</code> for the "QuandlEOD" Node.
 * 
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more
 * complex dialog please derive directly from
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author I Tang Lo
 */
public class QuandlEODNodeDialog extends NodeDialogPane {

	private FilesHistoryPanel contentPane;
	private static JTable table;
	private JTextField apiKeyTF;
	private JDatePickerImpl endDate;
	private JDatePickerImpl startDate;
	private JComboBox<String> tickersCCB;
	private DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>();
	private String currentTickers;

	protected QuandlEODNodeDialog() {
		super();

		if (QuandlEODNodeModel.m_config ==null)
			QuandlEODNodeModel.m_config = new QuandlEODConfig();
//		this.getPanel().getParent().getClass();
//		
//		System.out.println(this.getPanel().getParent().getClass());
		
		
		addTab("EOD", initLayout());
		

	}

	private JPanel initLayout() {

//    	String[] list = new String[] { "arthur", "brian", "chet", "danny", "dave",  "don", "edmond", "eddy", "glen", "gregory", "john",  "ken", "malcolm", "pete", "stephanie", "yvonne" };

		contentPane = new FilesHistoryPanel(createFlowVariableModel("url", FlowVariable.Type.STRING),
                "csv_read", LocationValidation.FileInput, ".csv", ".txt");
		contentPane.setBorder(new EmptyBorder(50, 50, 50, 50));
		contentPane.setLayout(null);
		contentPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		contentPane.setSize(1000, 1000);
		

		table = new JTable();
		initJTable();

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(17, 113, 250, 53);
		scrollPane.setViewportView(table);
		contentPane.add(scrollPane);

		JLabel lblStep_1 = new JLabel("Step 1 : Enter an API Key");
		lblStep_1.setBounds(17, 19, 158, 16);
		contentPane.add(lblStep_1);

		apiKeyTF = new JTextField();
		apiKeyTF.setBounds(178, 14, 191, 26);
		contentPane.add(apiKeyTF);
		apiKeyTF.setColumns(10);

		JLabel lblStep_2 = new JLabel("Step 2 : Choose Market");
		lblStep_2.setBounds(17, 49, 158, 16);
		contentPane.add(lblStep_2);

		tickersCCB = new AutoCompleteComboBox(new QuandlEODTickerList().getTickerNASDAQ());
		tickersCCB.setBounds(178, 75, 191, 18);
		contentPane.add(tickersCCB);

		JLabel lblStep_3 = new JLabel("Step 3 : Choose Tickers");
		lblStep_3.setBounds(17, 80, 158, 16);
		contentPane.add(lblStep_3);

		JComboBox comboBox = new JComboBox(new String[] { "NASDAQ", "NYSE", "NYSE Arca", "NYSE MKT" });
		comboBox.setBounds(178, 45, 191, 27);
		comboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {

				int selectedItem = comboBox.getSelectedIndex();
				if (selectedItem == 0) {

					Object[] obj = new QuandlEODTickerList().getTickerNASDAQ();

					model.removeAllElements();

					for (Object s : obj) {
						model.addElement(String.valueOf(s));
					}
					tickersCCB.setModel(model);

					tickersCCB.revalidate();
					contentPane.revalidate();
					tickersCCB.repaint();
					contentPane.repaint();

				} else if (selectedItem == 1) {

					Object[] obj = new QuandlEODTickerList().getTickerNYSE();

					model.removeAllElements();

					for (Object s : obj) {
						model.addElement(String.valueOf(s));
					}
					tickersCCB.setModel(model);
					
					tickersCCB.revalidate();
					contentPane.revalidate();
					tickersCCB.repaint();
					contentPane.repaint();

				} else if (selectedItem == 2) {

					Object[] obj = new QuandlEODTickerList().getTickerNYSEArca();

					model.removeAllElements();

					for (Object s : obj) {
						model.addElement(String.valueOf(s));
					}
					tickersCCB.setModel(model);

					tickersCCB.revalidate();
					contentPane.revalidate();
					tickersCCB.repaint();
					contentPane.repaint();

				} else if (selectedItem == 3) {

					Object[] obj = new QuandlEODTickerList().getTickerNYSEMKT();

					model.removeAllElements();

					for (Object s : obj) {
						model.addElement(String.valueOf(s));
					}
					tickersCCB.setModel(model);

					tickersCCB.revalidate();
					contentPane.revalidate();
					tickersCCB.repaint();
					contentPane.repaint();
				}

			}
		});
		contentPane.add(comboBox);

		JLabel lblStep_4 = new JLabel("Step 4 : Choose Date Range (Optional)");
		lblStep_4.setBounds(17, 178, 248, 16);
		contentPane.add(lblStep_4);

		UtilDateModel startModel = new UtilDateModel();
		UtilDateModel endModel = new UtilDateModel();
		JDatePanelImpl startDatePanel = new JDatePanelImpl(startModel, new Properties());
		JDatePanelImpl endDatePanel = new JDatePanelImpl(endModel, new Properties());

		JLabel lblStartDate = new JLabel("Start :");
		lblStartDate.setBounds(17, 200, 45, 16);
		contentPane.add(lblStartDate);

		startDate = new JDatePickerImpl(startDatePanel, new DateLabelFormatter());
		startDate.setBounds(60, 195, 207, 26);
		startDate.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				Date end = (Date) endDate.getModel().getValue();
				Date start = (Date) startDate.getModel().getValue();

				if (end != null && start != null && end.getTime() < start.getTime()) {
					JOptionPane.showMessageDialog(null, "End Date earlier than Start Date", "Wrong Selection",
							JOptionPane.WARNING_MESSAGE);
				}

			}
		});
		contentPane.add(startDate);

		JLabel lblEndDate = new JLabel("End :");
		lblEndDate.setBounds(17, 220, 36, 16);
		contentPane.add(lblEndDate);

		endDate = new JDatePickerImpl(endDatePanel, new DateLabelFormatter());
		endDate.setBounds(60, 220, 207, 26);
		endDate.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				Date end = (Date) endDate.getModel().getValue();
				Date start = (Date) startDate.getModel().getValue();

				if (end != null && start != null && end.getTime() < start.getTime()) {
					JOptionPane.showMessageDialog(null, "End Date earlier than Start Date", "Wrong Selection",
							JOptionPane.WARNING_MESSAGE);
				}

			}
		});
		contentPane.add(endDate);

		JButton addBtn = new JButton("Add");
		addBtn.setBounds(279, 110, 90, 27);
		addBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				String ticker = (String.valueOf(tickersCCB.getSelectedItem())).toUpperCase();

				System.out.println("select " + ticker);

				// if ticker already in chose list
				if (QuandlEODNodeModel.m_tickers.indexOf(ticker.toUpperCase()) == -1) {

					if (checkData(ticker)) {
						QuandlEODNodeModel.m_tickers.add(ticker);
						updateTickersString(); 
					}
					else
						JOptionPane.showMessageDialog(null, "Data is premium / Unavailable", "Invalid",
								JOptionPane.ERROR_MESSAGE);

				} else {
					JOptionPane.showMessageDialog(null, "Ticker Already in the list", "", JOptionPane.WARNING_MESSAGE);
				}
				System.out.println("tickers size = " + QuandlEODNodeModel.m_tickers.size());

				initJTable();
				
				
			}

		});
		contentPane.add(addBtn);

		JButton removeBtn = new JButton("Remove");
		removeBtn.setBounds(279, 139, 90, 27);
		removeBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				QuandlEODNodeModel.m_tickers.remove(table.getSelectedRow());
				System.out.println("tickers size = " + QuandlEODNodeModel.m_tickers.size());
				System.out.println("select " + tickersCCB.getSelectedItem().toString());
				
				initJTable();

				updateTickersString();
			}

		});
		contentPane.add(removeBtn);

		return contentPane;
	}

	/** {@inheritDoc} */
	@Override
	protected void loadSettingsFrom(final NodeSettingsRO settings, final DataTableSpec[] specs)
			throws NotConfigurableException {

		System.out.println("QuandlEODNodeDialog: loadSettingsFrom()");

		
		QuandlEODConfig config = new QuandlEODConfig();
		config.loadSettingsInDialog(settings);
		contentPane.updateHistory();
			
		
//		apiKeyTF.setText(QuandlEODNodeModel.m_config.getm_api_key());

		System.out.println("m_tickers.size() = " + QuandlEODNodeModel.m_tickers.size());
		System.out.println("m_tickers.getm_api_key() = " + QuandlEODNodeModel.m_config.getm_api_key()
				+ "m_tickers.getM_start_date() = " + QuandlEODNodeModel.m_config.getM_start_date()
				+ "m_tickers.getM_end_date() = " + QuandlEODNodeModel.m_config.getM_end_date());

	}

	/** {@inheritDoc} */
	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) throws InvalidSettingsException {

		System.out.println("QuandlEODNodeDialog: saveSettingsTo()");
		

		QuandlEODConfig config = new QuandlEODConfig();
		
		config.setm_api_key(apiKeyTF.getText());
		Date selectedStartDate = (Date) startDate.getModel().getValue();
		if(selectedStartDate!=null)
			config.setM_start_date(convertDate(selectedStartDate));
		Date selectedEndDate = (Date) endDate.getModel().getValue();
		if(selectedEndDate!=null)
			config.setM_end_date(convertDate(selectedEndDate));
		if(currentTickers!=null)
		    config.setM_tickers(currentTickers);
		
		System.out.println("convertDate(selectedStartDate) = " + convertDate(selectedStartDate));
		System.out.println("convertDate(selectedEndDate) = " + convertDate(selectedEndDate));

		System.out.println("m_tickers.size() = " + QuandlEODNodeModel.m_tickers.size());
		System.out.println("m_tickers.getm_api_key() = " + QuandlEODNodeModel.m_config.getm_api_key()
				+ "m_tickers.getM_start_date() = " + QuandlEODNodeModel.m_config.getM_start_date()
				+ "m_tickers.getM_end_date() = " + QuandlEODNodeModel.m_config.getM_end_date());
		config.saveSettingsTo(settings);
		contentPane.addToHistory();
	
	}

	public static void initJTable() {

		DefaultTableModel dtm = (DefaultTableModel) table.getModel();

		Object[][] data = new Object[QuandlEODNodeModel.m_tickers.size()][1];

		for (int i = 0; i < QuandlEODNodeModel.m_tickers.size(); i++) {

			data[i][0] = QuandlEODNodeModel.m_tickers.get(i);

		}

		Object[] columnName = new Object[] { "Ticker" };

		dtm = new DefaultTableModel(data, columnName) {

			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table.setTableHeader(null);
		table.setModel(dtm);

//		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
//		centerRenderer.setHorizontalAlignment(SwingConstants.LEFT);
//		TableModel tableModel = table.getModel();
//		for (int columnIndex = 0; columnIndex < tableModel.getColumnCount(); columnIndex++) {
//			table.getColumnModel().getColumn(columnIndex).setCellRenderer(centerRenderer);
//		}

	}

	public String convertDate(Date date) {
		if (date ==null)
			return "";
		String pattern = "yyyy-MM-dd";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		return simpleDateFormat.format(date);
	}

	private boolean checkData(String ticker) {


		String urlStr = "https://www.quandl.com/api/v3/datasets/EOD/" + ticker
				+ ".json?start_date=2018-10-31&end_date=2018-10-31&api_key="
				+ QuandlEODNodeModel.m_config.getm_api_key();

		System.out.println("urlStr = " + urlStr);

		JSONObject searchJSONObj = QuandlEODNodeModel.getJSONObjectFromURL(urlStr);

		if (searchJSONObj == null)
			return false;
		else
			return true;
	}
	private void updateTickersString() {
		
		currentTickers="";
		for(String ticker : QuandlEODNodeModel.m_tickers) {
			currentTickers+=ticker;
		}
	}
}

class DateLabelFormatter extends AbstractFormatter {

	private static final long serialVersionUID = 1L;
	
	private String datePattern = "yyyy-MM-dd";
	private SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

	@Override
	public Object stringToValue(String text) throws ParseException {
		return dateFormatter.parseObject(text);
	}

	@Override
	public String valueToString(Object value) throws ParseException {
		if (value != null) {
			GregorianCalendar cal = (GregorianCalendar) value;
			dateFormatter.setCalendar(cal);
			return dateFormatter.format(cal.getTime());
		}

		return "";
	}

}