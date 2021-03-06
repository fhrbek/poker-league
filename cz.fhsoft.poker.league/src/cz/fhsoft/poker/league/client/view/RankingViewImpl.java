package cz.fhsoft.poker.league.client.view;

import java.util.List;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.RowStyles;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Widget;

import cz.fhsoft.poker.league.client.cellview.AlignedColumn;
import cz.fhsoft.poker.league.client.util.Formatter;
import cz.fhsoft.poker.league.shared.services.RankingRecord;

public class RankingViewImpl extends Composite implements RankingView {
	
	private static RankingUiBinder uiBinder = GWT
			.create(RankingUiBinder.class);

	@UiTemplate("RankingView.ui.xml")
	interface RankingUiBinder extends UiBinder<Widget, RankingViewImpl> {
	}

	interface LocalStyle extends CssResource {
		String rowValid();
		
		String rowScratched();

		String colPlayer();

		String colAttendance();
	}

	@UiField
	LocalStyle style;

	@SuppressWarnings("unused")
	private Presenter presenter;

	@UiField
	CellTable<RankingRecord> rankingTable;
	
	public RankingViewImpl(boolean showNumberOfGames) {
		initWidget(uiBinder.createAndBindUi(this));
		
		rankingTable.setRowStyles(new RowStyles<RankingRecord>() {
		    @Override
		    public String getStyleNames(RankingRecord record, int rowIndex) {
		        if ((double) record.getGamesPlayed() / (double) record.getTotalGames() * 100 >= record.getMinimalAttendance()) {
		            return style.rowValid();
		        } else {
		            return style.rowScratched();
		        } 
		    }});

		Column<RankingRecord, String> column;

		rankingTable.addColumn(column = new Column<RankingRecord, String>(new TextCell()) {

			@Override
			public String getValue(RankingRecord record) {
				return record.getInGameFlag() == 0
						? "<ve hře>"
						: ((record.isSplit() ? "T" : "") + record.getRank());
			}
			
		}, "Pořadí");
		
		rankingTable.addColumn(column = new Column<RankingRecord, String>(new TextCell()) {

			@Override
			public String getValue(RankingRecord record) {
				return record.getPlayerNick();
			}
			
		}, "Hráč");
		column.setCellStyleNames(style.colPlayer());
		
		if(showNumberOfGames) {
			rankingTable.addColumn(column = new Column<RankingRecord, String>(new TextCell()) {

				@Override
				public String getValue(RankingRecord record) {
					StringBuilder sb = new StringBuilder();
					sb.append(record.getGamesPlayed());
					sb.append('/');
					sb.append(record.getTotalGames());
					sb.append(" (");
					sb.append(Formatter.format((double) record.getGamesPlayed() / (double) record.getTotalGames() * 100, 0));
					sb.append("%)");
					return sb.toString();
				}
				
			}, "Účast");
		  column.setCellStyleNames(style.colAttendance());
		}
		
		rankingTable.addColumn(new AlignedColumn<RankingRecord, String>(new TextCell(), HasHorizontalAlignment.ALIGN_RIGHT) {
	
			@Override
			public String getValue(RankingRecord record) {
				return "" + record.getBuyIns();
			}
	
		}, "Vklad");
		
		rankingTable.addColumn(new AlignedColumn<RankingRecord, String>(new TextCell(), HasHorizontalAlignment.ALIGN_RIGHT) {
		
			@Override
			public String getValue(RankingRecord record) {
				return Formatter.format(record.getPrizeMoney(), 0);
			}
		
		}, "Výhra");
		
		rankingTable.addColumn(new AlignedColumn<RankingRecord, String>(new TextCell(), HasHorizontalAlignment.ALIGN_RIGHT) {
		
			@Override
			public String getValue(RankingRecord record) {
				return Formatter.format(record.getPoints(), 2);
			}
		
		}, "Body");
		
		if(showNumberOfGames) {
			rankingTable.addColumn(new AlignedColumn<RankingRecord, String>(new TextCell(), HasHorizontalAlignment.ALIGN_RIGHT) {
				
				@Override
				public String getValue(RankingRecord record) {
					return Formatter.format(record.getRelativePrizeMoney()*100, 2) + "%";
				}
			
			}, "Relativní výhra");
			
			rankingTable.addColumn(new AlignedColumn<RankingRecord, String>(new TextCell(), HasHorizontalAlignment.ALIGN_RIGHT) {
			
				@Override
				public String getValue(RankingRecord record) {
					return Formatter.format(record.getRelativePoints(), 2);
				}
			
			}, "Relativní body");
			
			for(int i=1; i <= 4; i++) {
				final int rank = i;
				rankingTable.addColumn(new AlignedColumn<RankingRecord, String>(new TextCell(), HasHorizontalAlignment.ALIGN_RIGHT) {
				
					@Override
					public String getValue(RankingRecord record) {
						int[] rankCount = record.getRankCount(rank); 
						return rankCount[0] == rankCount[1]
								? ("" + rankCount[0])
								: (rankCount[0] + "(+" + (rankCount[1]-rankCount[0]) + ")");
					}
				
				}, rank + ".");
			}

			rankingTable.addColumn(new AlignedColumn<RankingRecord, String>(new TextCell(), HasHorizontalAlignment.ALIGN_RIGHT) {
				
				@Override
				public String getValue(RankingRecord record) {
					return "" + record.getBubbles();
				}
			
			}, "Bub");
		}
	}
	
	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void setRecords(List<RankingRecord> records) {
		if(records == null)
			rankingTable.setRowCount(0, false);
		else
			rankingTable.setRowData(records);
	}
}
