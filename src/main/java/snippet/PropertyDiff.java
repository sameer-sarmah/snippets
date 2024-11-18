package snippet;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringSubstitutor;



public class PropertyDiff {

	public static void main(String[] args) {
		//findUncommonKeys("publisher-messagebundle.properties","sfmessages-MESSAGE_PUBLISHER.properties.utf8");
		//findUncommonKeys("EVENT_SUBSCRIPTION","event-messagebundle.properties","sfmessages-EVENT_SUBSCRIPTION.properties.utf8");
		//findUncommonKeys("SEF_EVENT","event-messagebundle.properties","sfmessages-SEF.properties.utf8");
		//findUncommonKeys("xm_messageBundle.properties","sfmessages-XM.properties.utf8");
		//findUncommonKeys("IC_messagebundle.properties","sfmessages-INTEGRATION_BUILDER.properties.utf8");
		//findUncommonKeys("SC_messagebundle.properties","sfmessages-INTEGRATION_BUILDER.properties.utf8");
		//findUncommonKeys("event-messagebundle.properties","sfmessages-INTEGRATION_BUILDER_EXT.properties.utf8");
		
	}
	
	private static void findUncommonKeys(String prefix,String messageBundleFileName,String sfMessagesFileName) {
		createConfig(messageBundleFileName).ifPresent(messageBundle -> {
			  List<String> keysInMessageBundle =  getList(messageBundle.getKeys());
			  createConfig(sfMessagesFileName).ifPresent(sfmessages -> {
				  List<String> keysInSfMessages =  getList(sfmessages.getKeys());
				  Collection<String> uncommonKeys = CollectionUtils.subtract(keysInMessageBundle,keysInSfMessages);
				  uncommonKeys = uncommonKeys								
									    .stream()
										.filter(key -> StringUtils.startsWith(key, prefix))
										.collect(Collectors.toList());
				  Map<String,String> params = Map.of("UncommonKeys",String.valueOf(uncommonKeys),"KeysInMessageBundle",String.valueOf(keysInMessageBundle.size()),
						  "KeysInSfMessages",String.valueOf(keysInSfMessages.size()),"CountOfUncommonKeys",String.valueOf(uncommonKeys.size()));
				  String template = "KeysInMessageBundle=${KeysInMessageBundle},KeysInSfMessages=${KeysInSfMessages},CountOfUncommonKeys=${CountOfUncommonKeys},Keys present in only message bundle=${UncommonKeys}";
				  StringSubstitutor substitutor = new StringSubstitutor(params);
				  System.out.println(substitutor.replace(template));
			  });
		});
	}
	
	private static Stream<String> getStream(Iterator<String> iterator){
		return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(
                		iterator,
                        Spliterator.ORDERED)
                , false);
		
	}
	
	private static List<String> getList(Iterator<String> iterator){
		return getStream(iterator)
				.collect(Collectors.toList());
		
	}
	
	private static Optional<FileBasedConfiguration> createConfig(String fileName) {
		 Parameters params = new Parameters();
        FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
                new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
                        .configure(params.properties()
                                        .setFileName(fileName));
        try {
            return Optional.of(builder.getConfiguration());
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
        return Optional.empty();
	}
}
