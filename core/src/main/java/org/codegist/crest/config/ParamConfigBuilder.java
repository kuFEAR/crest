package org.codegist.crest.config;

import org.codegist.common.lang.Validate;
import org.codegist.crest.CRestProperty;
import org.codegist.crest.HttpRequest;

import java.util.Hashtable;
import java.util.Map;

@SuppressWarnings("unchecked")
public class ParamConfigBuilder<T extends ParamConfig> extends AbstractConfigBuilder<T> {

    private final MethodConfigBuilder parent;

    private final Map<String,Object> metas = new Hashtable<String, Object>();

    private String name;
    private String defaultValue;
    private String dest;


    // can we do without it ?
    public ParamConfigBuilder(Map<String, Object> customProperties) {
        this(null, customProperties);
    }
    
    ParamConfigBuilder(MethodConfigBuilder parent, Map<String, Object> customProperties) {
        super(customProperties);
        this.parent = parent;
    }

    /**
     * @inheritDoc
     */
    public T build(boolean validateConfig, boolean isTemplate) {
        // make local copies so that we don't mess with builder state to be able to call build multiple times on it
        String name = this.name;
        String defaultValue = this.defaultValue;
        String dest = this.dest;
        Map<String,Object> metas = this.metas;

        if (!isTemplate) {
            name = defaultIfUndefined(name, CRestProperty.CONFIG_PARAM_DEFAULT_NAME, MethodParamConfig.DEFAULT_NAME);
            defaultValue = defaultIfUndefined(defaultValue, CRestProperty.CONFIG_PARAM_DEFAULT_VALUE, MethodParamConfig.DEFAULT_VALUE);
            dest = defaultIfUndefined(dest, CRestProperty.CONFIG_PARAM_DEFAULT_DESTINATION, MethodParamConfig.DEFAULT_DESTINATION);
            metas = defaultIfUndefined(metas, CRestProperty.CONFIG_PARAM_DEFAULT_METAS, MethodParamConfig.DEFAULT_METADATAS);

        }

        if (validateConfig) {
            Validate.notBlank(name, "Parameter must have a name");
        }

        return (T) new DefaultParamConfig(name, defaultValue, dest, metas);
    }

    public MethodConfigBuilder endParamConfig() {
        return parent;
    }

    @Override
    public ParamConfigBuilder setIgnoreNullOrEmptyValues(boolean ignoreNullOrEmptyValues) {
        super.setIgnoreNullOrEmptyValues(ignoreNullOrEmptyValues);
        return this;
    }

    public ParamConfigBuilder setName(String name) {
        if (ignore(name)) return this;
        this.name = replacePlaceholders(name);
        return this;
    }


    public ParamConfigBuilder setDefaultValue(String defaultValue) {
        if (ignore(defaultValue)) return this;
        this.defaultValue = replacePlaceholders(defaultValue);
        return this;
    }

    public ParamConfigBuilder setDestination(String dest) {
        if (ignore(dest)) return this;
        this.dest = replacePlaceholders(dest);
        return this;
    }

    public ParamConfigBuilder setMetaDatas(Map<String,Object> metadatas) {
        if (ignore(metadatas)) return this;
        this.metas.clear();
        this.metas.putAll(metadatas);
        return this;
    }

    public ParamConfigBuilder addMetaDatas(String name, Object value) {
        if (ignore(name) && ignore(value)) return this;
        this.metas.clear();
        this.metas.put(name, value);
        return this;
    }

}