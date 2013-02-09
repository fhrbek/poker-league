package cz.fhsoft.poker.league.client.presenter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import cz.fhsoft.poker.league.client.persistence.ClientEntityManager;
import cz.fhsoft.poker.league.client.view.PrizeMoneyRuleSetView;
import cz.fhsoft.poker.league.client.widget.AbstractEntityListEditor.DataProvider;
import cz.fhsoft.poker.league.client.widget.AbstractEntityListEditor.LabeledColumn;
import cz.fhsoft.poker.league.client.widget.AbstractPersistentEntityEditor;
import cz.fhsoft.poker.league.client.widget.AbstractPersistentEntityListEditor;
import cz.fhsoft.poker.league.client.widget.AbstractTemporaryEntityEditor;
import cz.fhsoft.poker.league.client.widget.AbstractTemporaryEntityListEditor;
import cz.fhsoft.poker.league.shared.model.v1.PrizeMoneyFormula;
import cz.fhsoft.poker.league.shared.model.v1.PrizeMoneyRule;
import cz.fhsoft.poker.league.shared.model.v1.PrizeMoneyRuleSet;
import cz.fhsoft.poker.league.shared.persistence.Util;
import cz.fhsoft.poker.league.shared.persistence.compare.Comparators;
import cz.fhsoft.poker.league.shared.util.StringUtil;

public class PrizeMoneyRuleSetPresenter extends PresenterWithVersionedData implements PrizeMoneyRuleSetView.Presenter {
	
	private PrizeMoneyRuleSetView view;
	
	private AbstractPersistentEntityEditor<PrizeMoneyRuleSet> prizeMoneyRuleSetEditor = new AbstractPersistentEntityEditor<PrizeMoneyRuleSet>() {
		
		private Entry<TextBox> nameEntry = new Entry<TextBox>("Název", new TextBox()) {

			@Override
			public void setUpWidget(PrizeMoneyRuleSet entity) {
				getWidget().setText(entity.getName());
			}

			@Override
			public void updateEntity(PrizeMoneyRuleSet entity) {
				entity.setName(getWidget().getValue());
			}
			
		};

		private Entry<TextBox> descriptionEntry = new Entry<TextBox>("Popis", new TextBox()) {

			@Override
			public void setUpWidget(PrizeMoneyRuleSet entity) {
				getWidget().setText(entity.getDescription());
			}

			@Override
			public void updateEntity(PrizeMoneyRuleSet entity) {
				entity.setDescription(getWidget().getValue());
			}
			
		};
		
		private Entry<AbstractTemporaryEntityListEditor<PrizeMoneyRule>> prizeMoneyRulesEntry = new Entry<AbstractTemporaryEntityListEditor<PrizeMoneyRule>>(
				"Pravidla pro počet hráčů",
				new AbstractTemporaryEntityListEditor<PrizeMoneyRule>(
						"Nové pravidlo",
						new AbstractTemporaryEntityEditor<PrizeMoneyRule>() {

							private Entry<IntegerBox> numberOfPlayersEntry = new Entry<IntegerBox>("Počet hráčů", new IntegerBox()) {

								@Override
								public void setUpWidget(PrizeMoneyRule entity) {
									getWidget().setValue(entity.getNumberOfPlayers());
								}

								@Override
								public void updateEntity(PrizeMoneyRule entity) {
									entity.setNumberOfPlayers(getWidget().getValue());
								}

							};

							private Entry<AbstractTemporaryEntityListEditor<PrizeMoneyFormula>> prizeMoneyFormulasEntry = new Entry<AbstractTemporaryEntityListEditor<PrizeMoneyFormula>>(
									"Odměny dle umístění",
									new AbstractTemporaryEntityListEditor<PrizeMoneyFormula>(
											"Nová odměna",
											new AbstractTemporaryEntityEditor<PrizeMoneyFormula>() {

												private Entry<IntegerBox> rankEntry = new Entry<IntegerBox>(
														"Umístění", new IntegerBox()) {

													@Override
													public void setUpWidget(PrizeMoneyFormula entity) {
														getWidget().setValue(entity.getRank());
													}

													@Override
													public void updateEntity(PrizeMoneyFormula entity) {
														entity.setRank(getWidget().getValue());
													}

												};

												private Entry<IntegerBox> relativePrizeMoneyEntry = new Entry<IntegerBox>(
														"Odměna v % buy-in", new IntegerBox()) {

													@Override
													public void setUpWidget(PrizeMoneyFormula entity) {
														getWidget().setValue(entity.getRelativePrizeMoney());
													}

													@Override
													public void updateEntity(PrizeMoneyFormula entity) {
														entity.setRelativePrizeMoney(getWidget().getValue());
													}

												};

												{
													addEntry(rankEntry);
													addEntry(relativePrizeMoneyEntry);
												}
												
												@Override
												protected void copyAttributes(PrizeMoneyFormula source, PrizeMoneyFormula target) {
													target.setRank(source.getRank());
													target.setRelativePrizeMoney(source.getRelativePrizeMoney());
												}

												@Override
												protected SafeHtml getLabel(PrizeMoneyFormula entity) {
													return SafeHtmlUtils.fromString(entity.getId() > 0 ? ("Odměna pro " + entity.getRank() + ". místo")
																	: "Nová odměna");
												}

												@Override
												protected PrizeMoneyFormula createTemporaryEntity() {
													return new PrizeMoneyFormula();
												}

												@Override
												protected void validate(
														PrizeMoneyFormula entity,
														AsyncCallback<Map<Entry<? extends Widget>, String>> callback) {
													Map<Entry<? extends Widget>, String> errorMap = new HashMap<Entry<? extends Widget>, String>();
													if (entity.getRank() <= 0)
														errorMap.put(rankEntry,
																"Umístění musí být větší nebo rovno 1");
													if (entity.getRelativePrizeMoney() <= 0)
														errorMap.put(rankEntry,
																"Hodnota odměny musí být větší než 0");

													callback.onSuccess(errorMap);
												}

											}, null,
											new ArrayList<LabeledColumn<PrizeMoneyFormula>>() {
												private static final long serialVersionUID = 1L;

												{
													add(new LabeledColumn<PrizeMoneyFormula>(
															new Column<PrizeMoneyFormula, String>(
																	new TextCell()) {

																@Override
																public String getValue(PrizeMoneyFormula prizeMoneyFormula) {
																	return prizeMoneyFormula.getRank() + ". místo";
																}

															}, "Umístění"));

													add(new LabeledColumn<PrizeMoneyFormula>(
															new Column<PrizeMoneyFormula, String>(
																	new TextCell()) {

																@Override
																public String getValue(PrizeMoneyFormula prizeMoneyFormula) {
																	return prizeMoneyFormula.getRelativePrizeMoney() + "% buy-in";
																}

															}, "Odměna v % buy-in"));
												}
											}, Comparators.PRIZE_MONEY_FORMULA_COMPARATOR) {

										@Override
										protected PrizeMoneyFormula getNewEntity() {
											return new PrizeMoneyFormula();
										}

									}) {

								@Override
								public void setUpWidget(final PrizeMoneyRule entity) {
									getWidget().setDataProvider(new DataProvider<PrizeMoneyFormula>() {

										@Override
										protected void getData(final AsyncCallback<List<PrizeMoneyFormula>> callback) {
											Set<PrizeMoneyFormula> formulas = entity.getPrizeMoneyFormulas();
											Util.resolve(formulas,
													new AsyncCallback<Set<PrizeMoneyFormula>>() {

														@Override
														public void onFailure(Throwable caught) {
															callback.onFailure(caught);
														}

														@Override
														public void onSuccess(Set<PrizeMoneyFormula> resolvedFormulas) {
															List<PrizeMoneyFormula> formulaList = new ArrayList<PrizeMoneyFormula>(resolvedFormulas);
															Collections.sort(formulaList, Comparators.PRIZE_MONEY_FORMULA_COMPARATOR);
															callback.onSuccess(formulaList);
														}

													});
										}

									});
								}

								@Override
								public void updateEntity(PrizeMoneyRule entity) {
									List<PrizeMoneyFormula> formulas = getWidget().getEntities();

									// set/restore the reverse relationship
									for(PrizeMoneyFormula formula : formulas)
										formula.setPrizeMoneyRule(entity);

									entity.setPrizeMoneyFormulas(Util.asSet(formulas));
								}

							};

							{
								addEntry(numberOfPlayersEntry);
								addEntry(prizeMoneyFormulasEntry);
							}

							@Override
							protected void copyAttributes(PrizeMoneyRule source, PrizeMoneyRule target) {
								target.setNumberOfPlayers(source.getNumberOfPlayers());
								target.setPrizeMoneyFormulas(Util.cloneSet(source.getPrizeMoneyFormulas()));
							}

							@Override
							protected SafeHtml getLabel(PrizeMoneyRule entity) {
								return SafeHtmlUtils
										.fromString(entity.getId() > 0 ? "Pravidlo pro počet hráčů "
												+ entity.getNumberOfPlayers()
												: "Nové pravidlo");
							}

							@Override
							protected PrizeMoneyRule createTemporaryEntity() {
								return new PrizeMoneyRule();
							}

							@Override
							protected void validate(
									PrizeMoneyRule entity,
									AsyncCallback<Map<Entry<? extends Widget>, String>> callback) {
								Map<Entry<? extends Widget>, String> errorMap = new HashMap<Entry<? extends Widget>, String>();
								if (entity.getNumberOfPlayers() < 2)
									errorMap.put(numberOfPlayersEntry,
											"Minimální počet hráčů je 2");

								callback.onSuccess(errorMap);
							}

						}, null,
						new ArrayList<LabeledColumn<PrizeMoneyRule>>() {
							private static final long serialVersionUID = 1L;

							{
								add(new LabeledColumn<PrizeMoneyRule>(
										new Column<PrizeMoneyRule, String>(
												new TextCell()) {

											@Override
											public String getValue(
													PrizeMoneyRule prizeMoneyRule) {
												int num = prizeMoneyRule
														.getNumberOfPlayers();
												switch (num) {
												case 1:
													return "1 hráč";
												case 2:
												case 3:
												case 4:
													return num + " hráči";
												default:
													return num + " hráčů";
												}
											}

										}, "Počet hráčů"));
							}
						}, Comparators.PRIZE_MONEY_RULE_COMPARATOR) {

					@Override
					protected PrizeMoneyRule getNewEntity() {
						return new PrizeMoneyRule();
					}

				}) {

			@Override
			public void setUpWidget(final PrizeMoneyRuleSet entity) {
				getWidget().setDataProvider(new DataProvider<PrizeMoneyRule>() {

					@Override
					protected void getData(final AsyncCallback<List<PrizeMoneyRule>> callback) {
						Set<PrizeMoneyRule> rules = entity.getPrizeMoneyRules();
						Util.resolve(rules,
								new AsyncCallback<Set<PrizeMoneyRule>>() {

									@Override
									public void onFailure(Throwable caught) {
										callback.onFailure(caught);
									}

									@Override
									public void onSuccess(Set<PrizeMoneyRule> resolvedRules) {
										List<PrizeMoneyRule> ruleList = new ArrayList<PrizeMoneyRule>(resolvedRules);
										Collections.sort(ruleList, Comparators.PRIZE_MONEY_RULE_COMPARATOR);
										callback.onSuccess(ruleList);
									}

								});
					}

				});
			}

			@Override
			public void updateEntity(PrizeMoneyRuleSet entity) {
				List<PrizeMoneyRule> rules = getWidget().getEntities();

				// set/restore the reverse relationship
				for(PrizeMoneyRule rule : rules)
					rule.setPrizeMoneyRuleSet(entity);

				entity.setPrizeMoneyRules(Util.asSet(rules));
			}

		};
		
		{
			addEntry(nameEntry);
			addEntry(descriptionEntry);
			addEntry(prizeMoneyRulesEntry);
		}

		@Override
		protected SafeHtml getLabel(PrizeMoneyRuleSet entity) {
			return SafeHtmlUtils.fromString(entity.getId() > 0
					? "Pravidlo " + entity.getName()
					: "Nové pravidlo");
		}

		@Override
		protected PrizeMoneyRuleSet createTemporaryEntity() {
			return new PrizeMoneyRuleSet();
		}
		
		@Override
		protected PrizeMoneyRuleSet stripEntity(PrizeMoneyRuleSet prizeMoneyRuleSet) {
			if(!Util.proxify(prizeMoneyRuleSet.getPrizeMoneyRules()))
				for(PrizeMoneyRule prizeMoneyRule : prizeMoneyRuleSet.getPrizeMoneyRules())
					Util.proxify(prizeMoneyRule.getPrizeMoneyFormulas());

			return prizeMoneyRuleSet;
		}

		@Override
		protected void validate(PrizeMoneyRuleSet entity, AsyncCallback<Map<Entry<? extends Widget>, String>> callback) {
			Map<Entry<? extends Widget>, String> errorMap = new HashMap<Entry<? extends Widget>, String>();
			if(StringUtil.isEmpty(entity.getName(), true))
				errorMap.put(nameEntry, "Název musí být vyplněn");
			
			callback.onSuccess(errorMap);
		}
		
	};
	
	public PrizeMoneyRuleSetPresenter(Presenter parentPresenter, PrizeMoneyRuleSetView view) {
		super(parentPresenter);
		this.view = view;
		view.setPresenter(this);
		
		List<LabeledColumn<PrizeMoneyRuleSet>> columns = new ArrayList<LabeledColumn<PrizeMoneyRuleSet>>();

		columns.add(new LabeledColumn<PrizeMoneyRuleSet>(new Column<PrizeMoneyRuleSet, String>(new TextCell()) {

			@Override
			public String getValue(PrizeMoneyRuleSet player) {
				return player.getName();
			}
		}, "Název"));
		
		columns.add(new LabeledColumn<PrizeMoneyRuleSet>(new Column<PrizeMoneyRuleSet, String>(new TextCell()) {

			@Override
			public String getValue(PrizeMoneyRuleSet player) {
				return player.getDescription();
			}
		}, "Popis"));

		view.setPrizeMoneyRuleSetEditor(new AbstractPersistentEntityListEditor<PrizeMoneyRuleSet>("Nové pravidlo", prizeMoneyRuleSetEditor,
				new AbstractPersistentEntityListEditor.DataProvider<PrizeMoneyRuleSet>() {
			
			@Override
			protected void getData(final AsyncCallback<List<PrizeMoneyRuleSet>> callback) {
				ClientEntityManager.getInstance().list(PrizeMoneyRuleSet.class, new AsyncCallback<List<PrizeMoneyRuleSet>>() {

					@Override
					public void onFailure(Throwable caught) {
						callback.onFailure(caught);
					}

					@Override
					public void onSuccess(List<PrizeMoneyRuleSet> players) {
						Collections.sort(players, Comparators.PRIZE_MONEY_RULE_SET_COMPARATOR);
						callback.onSuccess(players);
					}
					
				});
			}
			
		},
		columns) {

			@Override
			protected PrizeMoneyRuleSet getNewEntity() {
				return new PrizeMoneyRuleSet();
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


	@Override
	public void updateForMode() {
		view.updateForMode();
	}
}
