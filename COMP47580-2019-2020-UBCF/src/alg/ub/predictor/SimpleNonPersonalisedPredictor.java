package alg.ub.predictor;

import java.util.Map;

import alg.ub.neighbourhood.Neighbourhood;
import profile.Profile;
import similarity.SimilarityMap;

public class SimpleNonPersonalisedPredictor implements Predictor {

	@Override
	public Double getPrediction(final Integer userId,final Integer itemId,final Map<Integer, Profile> userProfileMap,final Map<Integer, Profile> itemProfileMap, final Neighbourhood neighbourhood, final SimilarityMap simMap) {
		Profile item = itemProfileMap.get(itemId);
		if(item.getMeanValue() > 0)
			return new Double(item.getMeanValue());
		else
			return null;
	}

}
