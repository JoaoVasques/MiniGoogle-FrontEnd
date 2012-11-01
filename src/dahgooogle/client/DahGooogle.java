package dahgooogle.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class DahGooogle implements EntryPoint, ValueChangeHandler<String>
{
	private final GreetingServiceAsync ENDPOINT = GWT.create(GreetingService.class);

	@Override
	public void onValueChange(ValueChangeEvent<String> event)
	{
		RootPanel.get().clear();
		
		if(event.getValue().equals("!/list"))
		{
			ENDPOINT.getUserSearches("boda2", new AsyncCallback<String>()
			{
				@Override
				public void onFailure(Throwable caught) 
				{
					Window.alert(caught.getMessage());
				}

				@Override
				public void onSuccess(String result) 
				{
					RootPanel.get().clear();
					RootPanel.get().add(new HTML(result));
				}
					});
		}
		else if(event.getValue().equals("!/pissarra"))
		{
			RootPanel.get().add(new HTML("pissara"));
		}
		else
		{
			RootPanel.get().add(new HTML("enganaste-te!"));
		}
	}

	@Override
	public void onModuleLoad() 
	{
		History.addValueChangeHandler(this);
		
		final VerticalPanel contentPanel = new VerticalPanel();
		contentPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		contentPanel.getElement().getStyle().setProperty("marginLeft", "auto");
		contentPanel.getElement().getStyle().setProperty("marginRight", "auto");
		
		Image img = new Image("images/dah-gooogle.png");
		final Image loading = new Image("images/loading.gif");
		
		final MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
		
		// ADD common search terms
		ENDPOINT.getUserSearches("boda2", new AsyncCallback<String>()
		{
			@Override
			public void onFailure(Throwable caught) 
			{
				Window.alert(caught.getMessage());
			}

			@Override
			public void onSuccess(String result) 
			{
				result = result.substring(result.indexOf("boda2") + 6);
				
				if(result.length() > 2)
				{
					for(String search : result.split("\\|"))
					{
						oracle.add(search);	
					}
				}
			}
		});

		
		final SuggestBox searchBox = new SuggestBox(oracle);
		searchBox.getElement().getStyle().setWidth(435, Unit.PX);
		
		contentPanel.add(img);
		
		HorizontalPanel commandPanel = new HorizontalPanel();
		commandPanel.add(searchBox);
		
		VerticalPanel contentWrapper = new VerticalPanel();
		contentWrapper.getElement().getStyle().setProperty("marginLeft", "auto");
		contentWrapper.getElement().getStyle().setProperty("marginRight", "auto");
		contentWrapper.getElement().getStyle().setWidth(800, Unit.PX);
		
		final VerticalPanel contentResults = new VerticalPanel();
		
		contentWrapper.add(contentResults);
		
		Button searchButton = new Button("Search");
		searchButton.addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event)
			{
				contentResults.clear();
				
				contentResults.add(loading);
				ENDPOINT.saveUserQuery("boda2", searchBox.getText(), new AsyncCallback<String>()
				{	
					@Override
					public void onSuccess(String result) 
					{
						contentResults.clear();
						
						if(result.length() >5) contentResults.add(new HTML(result));
						else contentResults.add(new HTML("Term Not FOUND!"));
					}

					@Override
					public void onFailure(Throwable caught)
					{
						Window.alert(caught.getMessage());
					}
				});
			}
		});

		commandPanel.add(searchButton);
		contentPanel.add(commandPanel);
		
		RootPanel.get().add(contentPanel);
		RootPanel.get().add(contentWrapper);
	}
}
