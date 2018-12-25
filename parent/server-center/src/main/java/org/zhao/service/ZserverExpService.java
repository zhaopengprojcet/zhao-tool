package org.zhao.service;

import org.zhao.common.pojo.model.ZserverExpModel;
import org.zhao.common.util.view.ResultContent;

public interface ZserverExpService {

	ResultContent<String> save(ZserverExpModel model);
}
