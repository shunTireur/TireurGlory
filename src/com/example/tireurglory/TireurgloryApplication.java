package com.example.tireurglory;

import java.util.Map;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import com.vaadin.Application;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.terminal.ParameterHandler;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class TireurgloryApplication extends Application {

	/** シリアルバージョンID */
	private static final long serialVersionUID = -4222606611989855112L;

	/** データソース */
	private EmployeeContainer dataSource = EmployeeContainer
			.createWithTestData();

	private final String KEY_OAUTH_VERIFIER = "oauth_verifier";

	//TODO cunsumerKeyとconsumerSecretはサイトから都度カキコしてね。(GitHubに載せたら大変)
	//今度外部設定ファイルにだそうね。
	private final String CONSUMER_KEY = "******************";
	private final String CONSUMER_SECRET = "********************************";

	private Twitter twitter;
	private RequestToken requestToken;
	private AccessToken accessToken = null;
	private String oauthVerifier = null;

	private VerticalLayout twitterLayout;

	@Override
	public void init() {

		Window win = new Window();

		win.addParameterHandler(new ParameterHandler() {
			/** シリアルバージョンID */
			private static final long serialVersionUID = 5687066231589567470L;

			public void handleParameters(Map<String, String[]> parameters) {
				if (parameters.containsKey(KEY_OAUTH_VERIFIER)) {
					oauthVerifier = ((String[]) parameters
							.get(KEY_OAUTH_VERIFIER))[0];
				}

				if (oauthVerifier != null) {
					try {
						accessToken = twitter.getOAuthAccessToken(requestToken,
								oauthVerifier);
						twitter.setOAuthAccessToken(accessToken);
						twitterLayout.removeAllComponents();
						Label l = new Label(twitter.getScreenName());
						twitterLayout.addComponent(l);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});

		VerticalLayout layout = new VerticalLayout();
		layout.setSizeFull();
		try {
			twitter = new TwitterFactory().getInstance();
			twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);

			if (accessToken == null) {
				requestToken = twitter.getOAuthRequestToken();
				String url = requestToken.getAuthorizationURL();

				twitterLayout = new VerticalLayout();
				Link l = new Link("Auth", new ExternalResource(url));
				l.setDescription("認証");
				twitterLayout.addComponent(l);
				layout.addComponent(twitterLayout);
			}

		} catch (Exception e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

		win.setContent(layout);
		setMainWindow(win);

		// setMainWindow(new EmployeeWindow(this));

	}

	/**
	 * データソースを返却します。
	 *
	 * @return dataSource
	 */
	public EmployeeContainer getDataSource() {
		return dataSource;
	}
}
