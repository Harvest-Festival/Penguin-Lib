package uk.joshiejack.penguinlib.util;

public class PenguinRegistry {
    /* TODO? Switch to ForgeRegistries for most of these?
    public final ResourceLocation resource;

    @SuppressWarnings("unchecked")
    public <T extends PenguinRegistry> PenguinRegistry(Map<ResourceLocation, T> registry, ResourceLocation resource) {
        this.resource = resource;
        registry.put(resource, (T) this);
    }

    public ResourceLocation getRegistryName() {
        return resource;
    }

    public static class Item<E> extends PenguinRegistry implements IPenguinItemMap<E> {
        protected final String unlocalizedKey;
        protected final String unlocalizedName;
        protected final ModelResourceLocation itemModel;

        public <T extends PenguinRegistry> Item(String type, Map<ResourceLocation, T> registry, ResourceLocation resource) {
            super(registry, resource);
            this.unlocalizedKey = resource.getNamespace() + "." + type + "." + resource.getPath();
            this.unlocalizedName = unlocalizedKey + ".name";
            this.itemModel = new ModelResourceLocation(resource.getNamespace() + ":" + type + "#" + resource.getPath());
        }

        @Override
        public String getLocalizedName() {
            return StringHelper.localize(unlocalizedName);
        }

        @Override
        public ModelResourceLocation getItemModelLocation() {
            return itemModel;
        }
    }*/
}
