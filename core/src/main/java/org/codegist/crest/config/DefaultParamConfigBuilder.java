package org.codegist.crest.config;

import org.codegist.common.reflect.Types;
import org.codegist.crest.CRestConfig;
import org.codegist.crest.param.ParamProcessor;
import org.codegist.crest.param.ParamProcessorFactory;
import org.codegist.crest.serializer.Serializer;
import org.codegist.crest.util.Registry;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static org.codegist.crest.config.ParamConfig.*;
import static org.codegist.crest.config.ParamType.COOKIE;
import static org.codegist.crest.config.ParamType.HEADER;

@SuppressWarnings("unchecked")
class DefaultParamConfigBuilder extends ConfigBuilder implements ParamConfigBuilder {

    private final Map<String,Object> metas = new HashMap<String,Object>();

    private final Class<?> clazz;
    private final Type genericType;
    private final Registry<Class<?>, Serializer> classSerializerRegistry;

    private String name;
    private String defaultValue;
    private ParamType type;
    private String listSeparator;
    private Serializer serializer;
    private Boolean encoded;

    DefaultParamConfigBuilder(CRestConfig crestConfig, Map<Pattern,String> placeholders, Registry<Class<?>, Serializer> classSerializerRegistry, Class<?> clazz, Type genericType) {
        super(crestConfig, placeholders);
        this.clazz = Types.getComponentClass(clazz, genericType);
        this.genericType = Types.getComponentType(clazz, genericType);
        this.classSerializerRegistry = classSerializerRegistry;
    }

    /**
     * @inheritDoc
     */
    public ParamConfig build() throws Exception {
        String pName = defaultIfUndefined(this.name, PARAM_CONFIG_DEFAULT_NAME);
        String pDefaultValue = defaultIfUndefined(this.defaultValue, PARAM_CONFIG_DEFAULT_VALUE);
        ParamType pType = defaultIfUndefined(this.type, PARAM_CONFIG_DEFAULT_TYPE, ParamType.getDefault());
        Map<String,Object> pMetas = defaultIfUndefined(this.metas, PARAM_CONFIG_DEFAULT_METAS);
        String pListSeparator = defaultIfUndefined(this.listSeparator, PARAM_CONFIG_DEFAULT_LIST_SEPARATOR, null);
        Serializer pSerializer = defaultIfUndefined(this.serializer, PARAM_CONFIG_DEFAULT_SERIALIZER);
        Boolean pEncoded = defaultIfUndefined(this.encoded, PARAM_CONFIG_DEFAULT_ENCODED, (COOKIE.equals(pType) || HEADER.equals(pType)));
        ParamProcessor paramProcessor =  defaultIfUndefined(null, PARAM_CONFIG_DEFAULT_PROCESSOR, ParamProcessorFactory.newInstance(pType, pListSeparator));

        if (pSerializer == null) {
            // if null, then choose which serializer to apply using default rules
            pSerializer = classSerializerRegistry.get(clazz, getCRestConfig());
        }

        return new DefaultParamConfig(
                genericType,
                clazz,
                pName,
                pDefaultValue,
                pType,
                pMetas,
                pSerializer,
                pEncoded,
                paramProcessor);
    }

    public ParamConfigBuilder setName(String name) {
        this.name = ph(name);
        return this;
    }


    public ParamConfigBuilder setDefaultValue(String defaultValue) {
        this.defaultValue = ph(defaultValue);
        return this;
    }

    public ParamConfigBuilder setType(ParamType type) {
        this.type = type;
        return this;
    }

    public ParamConfigBuilder setListSeparator(String listSeparator) {
        this.listSeparator = listSeparator;
        return this;
    }

    public ParamConfigBuilder setEncoded(boolean encoded) {
        this.encoded = encoded;
        return this;
    }

    public ParamConfigBuilder setMetaDatas(Map<String,Object> metadatas) {
        this.metas.clear();
        this.metas.putAll(metadatas);
        return this;
    }

    public ParamConfigBuilder setSerializer(Class<? extends Serializer> serializer) {
        this.serializer = newInstance(serializer);
        return this;
    }
}