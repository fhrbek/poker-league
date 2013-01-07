package cz.fhsoft.poker.league.client.presenter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import cz.fhsoft.poker.league.client.persistence.ClientEntityManager;
import cz.fhsoft.poker.league.client.view.PlayersView;
import cz.fhsoft.poker.league.client.widget.AbstractEntityListEditor.LabeledColumn;
import cz.fhsoft.poker.league.client.widget.AbstractPersistentEntityEditor;
import cz.fhsoft.poker.league.client.widget.AbstractPersistentEntityListEditor;
import cz.fhsoft.poker.league.shared.model.v1.Player;
import cz.fhsoft.poker.league.shared.util.StringUtil;

public class PlayersPresenter extends PresenterWithVersionedData implements PlayersView.Presenter {
	
	protected static final Comparator<Player> PLAYERS_COMPARATOR = new Comparator<Player>() {

		@Override
		public int compare(Player p1, Player p2) {
			return p1.getNick().compareTo(p2.getNick());
		}
		
	};

	private PlayersView view;
	
	private AbstractPersistentEntityEditor<Player> playerEditor = new AbstractPersistentEntityEditor<Player>() {
		
		private Entry<TextBox> nickEntry = new Entry<TextBox>("Přezdívka", new TextBox()) {

			@Override
			public void setUpWidget(Player entity) {
				getWidget().setText(entity.getNick());
			}

			@Override
			public void updateEntity(Player entity) {
				entity.setNick(getWidget().getValue());
			}
			
		};

		private Entry<TextBox> emailEntry = new Entry<TextBox>("E-mail", new TextBox()) {

			@Override
			public void setUpWidget(Player entity) {
				getWidget().setText(entity.getEmailAddress());
			}

			@Override
			public void updateEntity(Player entity) {
				entity.setEmailAddress(getWidget().getValue());
			}
			
		};
		
		private Entry<TextBox> firstNameEntry = new Entry<TextBox>("Jméno", new TextBox()) {

			@Override
			public void setUpWidget(Player entity) {
				getWidget().setText(entity.getFirstName());
			}

			@Override
			public void updateEntity(Player entity) {
				entity.setFirstName(getWidget().getValue());
			}
			
		};
		
		private Entry<TextBox> lastNameEntry = new Entry<TextBox>("Příjmení", new TextBox()) {

			@Override
			public void setUpWidget(Player entity) {
				getWidget().setText(entity.getLastName());
			}

			@Override
			public void updateEntity(Player entity) {
				entity.setLastName(getWidget().getValue());
			}
			
		};
		
		private Entry<CheckBox> activeEntry = new Entry<CheckBox>("Aktivní", new CheckBox()) {

			@Override
			public void setUpWidget(Player entity) {
				getWidget().setValue(entity.isActive());
			}

			@Override
			public void updateEntity(Player entity) {
				entity.setActive(getWidget().getValue());
			}
			
		};
		
		{
			addEntry(nickEntry);
			addEntry(emailEntry);
			addEntry(firstNameEntry);
			addEntry(lastNameEntry);
			addEntry(activeEntry);
		}

		@Override
		protected SafeHtml getLabel(Player entity) {
			return SafeHtmlUtils.fromString(entity.getId() > 0
					? "Hráč " + entity.getNick()
					: "Nový hráč");
		}

		@Override
		protected Player createTemporaryEntity() {
			return new Player();
		}

		@Override
		protected void validate(Player entity, AsyncCallback<Map<Entry<? extends Widget>, String>> callback) {
			Map<Entry<? extends Widget>, String> errorMap = new HashMap<Entry<? extends Widget>, String>();
			if(StringUtil.isEmpty(entity.getNick(), true))
				errorMap.put(nickEntry, "Přezdívka musí být vyplněna");
			if(StringUtil.isEmpty(entity.getEmailAddress(), true))
				errorMap.put(emailEntry, "E-mail musí být vyplněn");
			
			callback.onSuccess(errorMap);
		}
		
	};
	
	public PlayersPresenter(Presenter parentPresenter, PlayersView view) {
		super(parentPresenter);
		this.view = view;
		view.setPresenter(this);
		
		List<LabeledColumn<Player>> columns = new ArrayList<LabeledColumn<Player>>();

		columns.add(new LabeledColumn<Player>(new Column<Player, String>(new TextCell()) {

			@Override
			public String getValue(Player player) {
				return player.getNick();
			}
		}, "Přezdívka"));
		
		columns.add(new LabeledColumn<Player>(new Column<Player, String>(new TextCell()) {

			@Override
			public String getValue(Player player) {
				return player.getEmailAddress();
			}
		}, "e-mail"));

		columns.add(new LabeledColumn<Player>(new Column<Player, String>(new TextCell()) {

			@Override
			public String getValue(Player player) {
				return player.getFirstName();
			}
		}, "Jméno"));

		columns.add(new LabeledColumn<Player>(new Column<Player, String>(new TextCell()) {

			@Override
			public String getValue(Player player) {
				return player.getLastName();
			}
		}, "Příjmení"));

		columns.add(new LabeledColumn<Player>(new Column<Player, String>(new TextCell()) {

			@Override
			public String getValue(Player player) {
				return player.isActive() ? "Ano" : "Ne";
			}
		}, "Aktivní"));

		view.setPlayersEditor(new AbstractPersistentEntityListEditor<Player>("Nový hráč", playerEditor,
				new AbstractPersistentEntityListEditor.DataProvider<Player>() {
			
			@Override
			protected void getData(final AsyncCallback<List<Player>> callback) {
				ClientEntityManager.getInstance().list(Player.class, new AsyncCallback<List<Player>>() {

					@Override
					public void onFailure(Throwable caught) {
						callback.onFailure(caught);
					}

					@Override
					public void onSuccess(List<Player> players) {
						Collections.sort(players, PLAYERS_COMPARATOR);
						callback.onSuccess(players);
					}
					
				});
			}
			
		},
		columns) {

			@Override
			protected Player getNewEntity() {
				return new Player();
			}
			
		});
	}

	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(view.asWidget());
		refresh();
	}

	@Override
	protected void refresh() {
		view.refresh();
	}

}
