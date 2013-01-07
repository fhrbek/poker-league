package cz.fhsoft.poker.league.client.widget;

import java.util.List;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.fhsoft.poker.league.client.util.Dialog;
import cz.fhsoft.poker.league.client.util.Dialog.Option;
import cz.fhsoft.poker.league.client.util.ErrorReporter;
import cz.fhsoft.poker.league.shared.model.v1.IdentifiableEntity;

public abstract class AbstractEntityListEditor<E extends IdentifiableEntity> extends CellTable<E> {
	
	public static abstract class DataProvider<E extends IdentifiableEntity> {
		abstract protected void getData(AsyncCallback<List<E>> callback);
	}
	
	public static class LabeledColumn<E extends IdentifiableEntity> {
		private Column<E, ?> column;
		
		private SafeHtml label;
		
		public LabeledColumn(Column<E, ?> column, SafeHtml label) {
			this.column = column;
			this.label = label;
		}

		public LabeledColumn(Column<E, ?> column, String label) {
			this.column = column;
			this.label = SafeHtmlUtils.fromString(label);
		}
	}

	private static final String ENTITY_ID_ATTRIBUTE = "_entity_id";

	private static final String ENTITY_ACTION_ATTRIBUTE = "_entity_action";
	
	private static final String ACTION_NEW = "new";

	private static final String ACTION_EDIT = "edit";

	private static final String ACTION_REMOVE = "remove";

	interface SHtmlTemplates extends SafeHtmlTemplates {
		@Template("<span " + ENTITY_ID_ATTRIBUTE + "='{0}'><span "
				+ ENTITY_ACTION_ATTRIBUTE + "='" + ACTION_EDIT
				+ "' class='pseudoLink'>Upravit</span> <span "
				+ ENTITY_ACTION_ATTRIBUTE + "='" + ACTION_REMOVE
				+ "' class='pseudoLink'>Odstranit</span></span>")
		SafeHtml actions(int entityId);
		
		@Template("<span " + ENTITY_ACTION_ATTRIBUTE + "='" + ACTION_NEW + "' class='pseudoLink'>{0}</span>")
		SafeHtml newEntityLink(String text);
	}
	
	private static final SHtmlTemplates sHtmlTemplates = GWT.create(SHtmlTemplates.class);

	private SafeHtml newEntityLink;
	
	private DataProvider<E> dataProvider;
	
	protected Class<E> entityClass = null;
	
	public AbstractEntityListEditor(String newEntityLinkText, final AbstractEntityEditor<E> entityEditor, DataProvider<E> dataProvider, List<LabeledColumn<E>> columns) {
		
		this.dataProvider = dataProvider;

		newEntityLink = sHtmlTemplates.newEntityLink(newEntityLinkText);
		
		for(LabeledColumn<E> column : columns)
			addColumn(column.column, column.label);

		addColumn(new Column<E, SafeHtml>(
				new AbstractCell<SafeHtml>("click") {
					@Override
					public void render(Context context, SafeHtml value,
							SafeHtmlBuilder sb) {
						if (value != null) {
							sb.append(value);
						}
					}

					@Override
					public void onBrowserEvent(Context context, Element parent,
							SafeHtml value, NativeEvent event,
							ValueUpdater<SafeHtml> valueUpdater) {

						Element element = event.getEventTarget().cast();
						final int id = getEntityId(element);

						if (id != 0) {
							String action = getEntityAction(element);

							if (ACTION_EDIT.equals(action)) {
								getEntity(id, new AsyncCallback<E>() {

									@Override
									public void onFailure(Throwable caught) {
										ErrorReporter.error(caught);
									}

									@Override
									public void onSuccess(E entity) {
										entityEditor.setEntity(entity, new AsyncCallback<Void>() {

											@Override
											public void onFailure(Throwable caught) {
												ErrorReporter.error(caught);
											}

											@Override
											public void onSuccess(Void result) {
												entityEditor.showAsPopupPanel();
											}
											
										});
									}
									
								});
							}
							else if (ACTION_REMOVE.equals(action)) {
								Dialog.show("Potvrzení odstranění", "Opravdu chcete smazat tuto položku?",
										new Option("Ano", null) {

											@Override
											public void run() {
												removeItem(id);
											}
									
										},
										new Option("Ne", null));
							}
						}
					}

				}) {

			@Override
			public SafeHtml getValue(E entity) {
				return sHtmlTemplates.actions(entity.getId());
			}
		}, new Header<SafeHtml>(new AbstractCell<SafeHtml>("click") {

			@Override
			public void render(Context context, SafeHtml value, SafeHtmlBuilder sb) {
				sb.append(value);
			}
			
			@Override
			public void onBrowserEvent(Context context, Element parent,
					SafeHtml value, NativeEvent event,
					ValueUpdater<SafeHtml> valueUpdater) {

				Element element = event.getEventTarget().cast();
				String action = getEntityAction(element);

				if (ACTION_NEW.equals(action)) {
					entityEditor.setEntity(getNewEntity(), new AsyncCallback<Void>() {

						@Override
						public void onFailure(Throwable caught) {
							ErrorReporter.error(caught);
						}

						@Override
						public void onSuccess(Void result) {
							entityEditor.showAsPopupPanel();
						}
						
					});
				}
			}
		}) {

			@Override
			public SafeHtml getValue() {
				return newEntityLink;
			}
			
		});
		
		refresh();
	}
	
	protected abstract E getNewEntity();
	
	protected abstract void getEntity(int id, AsyncCallback<E> callback);

	public void refresh() {
		setEntities(null);

		if(dataProvider != null)
			dataProvider.getData(new AsyncCallback<List<E>>() {
	
				@Override
				public void onFailure(Throwable caught) {
					ErrorReporter.error(caught);
				}
	
				@Override
				public void onSuccess(List<E> entities) {
					setEntities(entities);
				}
				
			});
	}
	
	public void setDataProvider(DataProvider<E> dataProvider) {
		this.dataProvider = dataProvider;
		refresh();
	}

	private int getEntityId(Element element) {
		while (element != null) {
			if (element.hasAttribute(ENTITY_ID_ATTRIBUTE))
				return Integer.valueOf(element
						.getAttribute(ENTITY_ID_ATTRIBUTE));
			element = element.getParentElement();
		}

		return 0;
	}

	private String getEntityAction(Element element) {
		while (element != null) {
			if (element.hasAttribute(ENTITY_ACTION_ATTRIBUTE))
				return element
						.getAttribute(ENTITY_ACTION_ATTRIBUTE);
			element = element.getParentElement();
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	protected void setEntities(List<E> entities) {
		if(entities == null) {
			setRowCount(0, false);
			return;
		}

		if(entities.size() > 0)
			entityClass = (Class<E>) entities.get(0).getClass();

		setRowData(entities);
	}

	abstract protected void removeItem(int id);
}
