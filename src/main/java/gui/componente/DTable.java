package gui.componente;

import javax.swing.JTable;

@SuppressWarnings("serial")
public class DTable extends JTable {
			
	public DTable() {
		super();
		setRowHeight(20);
	}
	
	public void selectRow(int row) {
		if (row >= 0) {
			int maxLinha = getModel().getRowCount() - 1;
			if (maxLinha >= 0) {
				if (row > maxLinha)
					row = maxLinha;
				getSelectionModel().setSelectionInterval(row, row);
			}
		}
	}
	
	public void selectFirstRow() {
		selectRow(0);
	}
	
	public void selectLastRow() {
		selectRow(getModel().getRowCount() - 1);
	}
	
	/*
	@Override
	public Component prepareRenderer(TableCellRenderer renderer, int row, int col) {
		if (!(renderer instanceof DTableCellRenderer))
			renderer = new DTableCellRenderer();
        return super.prepareRenderer(renderer, row, col);
    }
    */

}
