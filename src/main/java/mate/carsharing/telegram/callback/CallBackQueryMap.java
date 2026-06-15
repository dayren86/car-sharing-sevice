package mate.carsharing.telegram.callback;

import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CallBackQueryMap {
    private final GetCarCallBackQuery getCarCallBackQuery;
    private final RentCarCallBackQuery rentCarCallBackQuery;
    private final RentDaysCallBackQuery rentDaysCallBackQuery;
    private final PayCallBackQuery payCallBackQuery;

    public Map<String, CallBackQuery> getCallBackMap() {
        Map<String, CallBackQuery> callBackQueryMap = new HashMap<>();
        callBackQueryMap.put("find", getCarCallBackQuery);
        callBackQueryMap.put("rentDays", rentDaysCallBackQuery);
        callBackQueryMap.put("rent", rentCarCallBackQuery);
        callBackQueryMap.put("pay", payCallBackQuery);

        return callBackQueryMap;
    }
}
