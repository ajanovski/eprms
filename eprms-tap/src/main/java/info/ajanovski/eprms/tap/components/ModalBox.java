package info.ajanovski.eprms.tap.components;

//Based on http://readyareyou.blogspot.com.au/2012/11/tapestry5-bootstrap-modal-dialog.html .

import javax.inject.Inject;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.http.services.Request;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;
import org.apache.tapestry5.services.ajax.JavaScriptCallback;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

@Import(module = { "ModalBox" })
public class ModalBox implements ClientElement {

	@Parameter(name = "componentClientId", value = "prop:componentResources.id",
			defaultPrefix = BindingConstants.LITERAL) private String																									componentClientId;

	@Inject private JavaScriptSupport																																		javaScriptSupport;
	@Inject private AjaxResponseRenderer																																	ajaxResponseRenderer;
	@Inject private Request																																						request;

	@Override
	public String getClientId() {
		return componentClientId;
	}

	void setupRender() {
		JSONObject json = new JSONObject();
		json.put("keyboard", true);
		json.put("backdrop", "static");
		json.put("focus", true);
		javaScriptSupport.require("ModalBox").invoke("activate")
				.with(componentClientId, json);
	}

	public void hide() {
		if (request.isXHR()) {
			ajaxResponseRenderer.addCallback(makeScriptToHideModal());
		}
	}

	private JavaScriptCallback makeScriptToHideModal() {
		return new JavaScriptCallback() {
			public void run(JavaScriptSupport javascriptSupport) {
				javaScriptSupport.require("ModalBox").invoke("hide")
						.with(componentClientId);
			}
		};
	}

}
