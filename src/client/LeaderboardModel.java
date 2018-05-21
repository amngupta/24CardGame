package client;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import records.UserStatistics;

public class LeaderboardModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;

	private final List<UserStatistics> leaderboardList;
    
	private static final String[] columnNames = {
			"Rank",
            "Name",
            "Games Won",
            "Games Played",
            "Total Time Played (s)",
            "Average Time (s)"};

	
    @SuppressWarnings("rawtypes")
	private final Class[] columnClass = new Class[] {
        Integer.class, String.class, Integer.class, Integer.class, float.class, float.class
    };


    LeaderboardModel(List<UserStatistics> stats)
    {
    	this.leaderboardList = stats;
    }
    
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		UserStatistics row = leaderboardList.get(rowIndex);
    	switch (columnIndex) {
        case 0:
            return row.getRank();
        case 1:
        	return row.getUsername();
        case 2:
        	return row.getNumberOfWins();
        case 3:
        	return row.getNumberOfGames();
        case 4:
        	return (row.getTotalTimePlayed()/1000);
        case 5:
        	return row.getAvgTime();
        }
        return null;
    }
	
	@Override
	public String getColumnName(int column)
    {
        return columnNames[column];
    }
 
    @Override
    public Class<?> getColumnClass(int columnIndex)
    {
        return columnClass[columnIndex];
    }
	 
    @Override
    public int getColumnCount()
    {
        return columnNames.length;
    }
	 
    @Override
    public int getRowCount()
    {
    	int size; 
        if (leaderboardList == null) { 
           size = 0; 
        } 
        else { 
           size = leaderboardList.size(); 
        } 
        return size;     
    }
	 
}
