package com.cellsgame.game.core.module.config;

/**
 * @author Aly on  2016-10-24.
 */
public abstract class ModuleConfig<T> implements Buildable<Module> {
    ModuleBuilder<T> moduleBuilder;

    ModuleConfig(ModuleBuilder<T> config) {
        this.moduleBuilder = config;
        config.configs.put(this.getClass(), this);
    }

    @Override
    public Module<T> build() {
        return moduleBuilder.build();
    }

    abstract void buildTO(Module<T> module);
}
