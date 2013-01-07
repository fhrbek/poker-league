package cz.fhsoft.poker.league.client.widget;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

import cz.fhsoft.poker.league.client.persistence.ClientEntityManager;
import cz.fhsoft.poker.league.client.util.ErrorReporter;
import cz.fhsoft.poker.league.shared.model.v1.IdentifiableEntity;

public abstract class AbstractEntityEditor<E extends IdentifiableEntity> extends FlowPanel {
	
	public abstract class Entry<W extends Widget> {

		private String label;

		private W widget;

		public Entry(String label, W widget) {
			this.label = label;
			this.widget = widget;
		}
		
		public W getWidget() {
			return widget;
		}
		
		abstract public void setUpWidget(E entity);
		
		abstract public void updateEntity(E entity);
	}
	
	private E entity;
	
	private List<Entry<? extends Widget>> entries;
	
	private PopupPanel popupPanel = null;
	
	public AbstractEntityEditor() {
		entity = null;
		entries = new ArrayList<Entry<? extends Widget>>();
	}
	
	public void addEntry(Entry<? extends Widget> entry) {
		this.entries.add(entry);
	}

	public void setEntity(E entity, final AsyncCallback<Void> callback) {
		ClientEntityManager.getInstance().resolveEntity(entity, new AsyncCallback<E>() {

			@Override
			public void onFailure(Throwable caught) {
				callback.onFailure(caught);
			}

			@Override
			public void onSuccess(E resolvedEntity) {
				AbstractEntityEditor.this.entity = resolvedEntity;
				build();
				callback.onSuccess(null);
			}
			
		});
	}

	public void showAsPopupPanel() {
		if(entity == null)
			throw new IllegalArgumentException("No entity assigned");

		popupPanel = new PopupPanel();
		popupPanel.add(this);
		popupPanel.center();
	}
	
	protected void build() {
		
		clear();
		add(new HTML(getLabel(entity)));

		Grid grid = new Grid(entries.size(), 2);
		
		int row = 0;
		for(Entry<? extends Widget> entry : entries) {
			entry.setUpWidget(entity);
			grid.setHTML(row, 0, SafeHtmlUtils.fromString(entry.label));
			grid.setWidget(row++, 1, entry.widget);
		}
		
		add(grid);
		
		Button okButton = new Button("OK");
		add(okButton);
		
		okButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				E testEntity = createTemporaryEntity();
				for(Entry<? extends Widget> entry : entries)
					entry.updateEntity(testEntity);

				validate(testEntity, new AsyncCallback<Map<Entry<? extends Widget>, String>>() {

					@Override
					public void onFailure(Throwable caught) {
						ErrorReporter.error(caught);
					}

					@Override
					public void onSuccess(Map<Entry<? extends Widget>, String> errorMap) {
						if(errorMap == null || errorMap.isEmpty()) {
							for(Entry<? extends Widget> entry : entries)
								entry.updateEntity(entity);
							
							save(entity, new AsyncCallback<Void>() {

								@Override
								public void onFailure(Throwable caught) {
									ErrorReporter.error(caught);
								}

								@Override
								public void onSuccess(Void result) {
									close();
								}
								
							});
						}
						else {
							//TODO Display error message somewhere in the window
						}
						
					}
					
				});
			}
			
		});
		
		Button cancelButton = new Button("Storno");
		add(cancelButton);
		
		cancelButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				close();
			}
			
		});
	}
	
	private void close() {
		onClose();
		AbstractEntityEditor.this.clear();
		if(popupPanel != null)
			popupPanel.hide();
	}

	protected void refresh() {
		// Do something if data changes...
	}
	
	protected SafeHtml getLabel(E entity) {
		return SafeHtmlUtils.fromString(entity.toString());
	}
	
	abstract protected E createTemporaryEntity();

	protected void validate(E entity, AsyncCallback<Map<Entry<? extends Widget>, String>> callback) {
		callback.onSuccess(Collections.<Entry<? extends Widget>, String> emptyMap());
	}
	
	abstract protected void save(E entity, final AsyncCallback<Void> callback);
	
	protected void onClose() {
		// nothing by default
	}
}
