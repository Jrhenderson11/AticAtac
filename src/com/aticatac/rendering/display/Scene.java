package com.aticatac.rendering.display;

import java.util.LinkedList;

import com.aticatac.rendering.interfaces.Renderable;

public class Scene {
	
	
	// ------
	// Fields
	// ------
	
	
	/**
	 * The default layer name, this layer appears behind all other layers
	 */
	public static final String DEFAULT_LAYERNAME = "default";
	
	/**
	 * The list of layers of the scene, in order from background to foreground, each successive layer on top of the other
	 */
	private LinkedList<LinkedList<Renderable>> layers;
	
	/**
	 * The list of the layer names, in parallel with layers
	 */
	private LinkedList<String> layerNames;
	
	// ------------
	// Constructors
	// ------------
	
	
	/**
	 * The defauls constructor.
	 */
	public Scene() {
		this.layers = new LinkedList<LinkedList<Renderable>>();
		this.layerNames = new LinkedList<String>();
		addLayer(DEFAULT_LAYERNAME);
	}
	
	
	// -------
	// Methods
	// -------
	
	
	/**
	 * Add a new layer with the given name and list of components to the scene
	 * @param layerName The name of the layer
	 * @param components The list of components to add
	 * @return Returns false when the layer name has already been used.
	 */
	public boolean addLayer(String layerName, LinkedList<Renderable> components) {
		if (!layerNames.contains(layerName)) {
			layers.add(components);
			layerNames.add(layerName);
			return true;
		} else return true;
	}
	
	/**
	 * Adds a new layer with the given name to the scene.
	 * This layer will appear ON TOP of previous layers
	 * @param layerName The name of the layer
	 * @return Returns true if the layer has been added
	 */
	public boolean addLayer(String layerName) {
		return addLayer(layerName, new LinkedList<Renderable>());
	}
	
	/**
	 * Removes the layer with the given name from the scene. Cannot remove the default layer.
	 * @param layerName The name of the layer given when added.
	 * @return Returns true if the layer exists and has been removsed.
	 */
	public boolean removeLayer(String layerName) {
		if (layerNames.contains(layerName) && !layerName.equals(DEFAULT_LAYERNAME)) {
			int i = layerNames.indexOf(layerName);
			layers.remove(i);
			layerNames.remove(i);
			return true;
		} else return false;
	}
	
	/**
	 * Adds the component to the given layer of this scene.
	 * @param layerName
	 * @param component
	 * @return
	 */
	public boolean addComponent(String layerName, Renderable component) {
		if (layerNames.contains(layerName)) {
			return layers.get(layerNames.indexOf(layerName)).add(component);
		} else return false;
	}
	
	/**
	 * Adds the component to the default layer of this scene
	 * @param component The component to add
	 * @return Returns true if the component was added.
	 */
	public boolean addComponent(Renderable component) {
		return addComponent(DEFAULT_LAYERNAME, component);
	}
	
	/**
	 * Returns a full list of all components in the scene, in order of layers added.
	 * @return A LinkedList of components
	 */
	public LinkedList<Renderable> getSceneComponents() {
		LinkedList<Renderable> fullList = new LinkedList<Renderable>();
		for (LinkedList<Renderable> layer: layers) {
			fullList.addAll(layer);
		}
		return fullList;
	}
	
	/**
	 * Returns the layer index, 0 being the background layer, and each successive layer being on top of another.
	 * @param layerName The name of the layer
	 * @return The index of the layer
	 */
	public int getLayerPosition(String layerName) {
		return layerNames.indexOf(layerName);
	}
	
	/**
	 * Returns the name of the layer at the given index
	 * @param layerIndex The index of the layer to get the name of
	 * @return Retur
	 */
	public String getLayerName(int layerIndex) {
		if (layerIndex >= 0 && layerIndex < layerNames.size()) {
			return layerNames.get(layerIndex);
		} else {
			return null;
		}
	}
	
	/**
	 * Get the number of layers in the scene
	 * @return Returns the number of layers in the scene
	 */
	public int getNumLayers() {
		return layerNames.size();
	}
}