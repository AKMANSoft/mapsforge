/*
 * Copyright © 2015 Ludwig M Brinckmann
 *
 * This program is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.mapsforge.applications.android.samples;

import android.view.View;
import android.widget.Button;

import org.mapsforge.core.model.Rotation;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.android.util.AndroidUtil;
import org.mapsforge.map.layer.debug.TileCoordinatesLayer;
import org.mapsforge.map.layer.debug.TileGridLayer;
import org.mapsforge.map.layer.labels.LabelLayer;
import org.mapsforge.map.layer.labels.LabelStore;
import org.mapsforge.map.layer.labels.MapDataStoreLabelStore;
import org.mapsforge.map.layer.labels.ThreadedLabelLayer;
import org.mapsforge.map.layer.renderer.TileRendererLayer;

/**
 * Experimental viewer supporting map rotation, not production ready yet and with interface
 * changes to come.
 */
public class RotationMapViewer extends RenderTheme4 {

	private float rotationAngle;

	@Override
	protected void createControls() {
		super.createControls();

		// Three rotation buttons: rotate clockwise, reset, counterclockwise
		Button rotateCWButton = (Button) findViewById(R.id.rotateClockwiseButton);
		rotateCWButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				rotationAngle += 15;
				Rotation rotation = new Rotation(rotationAngle, mapView.getDimension().width / 2, mapView.getDimension().height / 2);
				RotationMapViewer.this.mapView.rotate(rotation);
				RotationMapViewer.this.redrawLayers();
			}
		});

		Button rotateResetButton = (Button) findViewById(R.id.rotateResetButton);
		rotateResetButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				rotationAngle = 0;
				Rotation rotation = new Rotation(rotationAngle, mapView.getDimension().width / 2, mapView.getDimension().height / 2);
				RotationMapViewer.this.mapView.rotate(rotation);
				RotationMapViewer.this.redrawLayers();
			}
		});

		Button rotateCCWButton = (Button) findViewById(R.id.rotateCounterClockWiseButton);
		rotateCCWButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				rotationAngle -= 15;
				Rotation rotation = new Rotation(rotationAngle, mapView.getDimension().width / 2, mapView.getDimension().height / 2);
				RotationMapViewer.this.mapView.rotate(rotation);
				RotationMapViewer.this.redrawLayers();
			}
		});

	}

	@Override
	protected void createLayers() {
		TileRendererLayer tileRendererLayer = AndroidUtil.createTileRendererLayer(this.tileCaches.get(0),
				mapView.getModel().mapViewPosition, getMapFile(), getRenderTheme(), false, false, false);
		this.mapView.getLayerManager().getLayers().add(tileRendererLayer);


		MapDataStoreLabelStore labelStore = new MapDataStoreLabelStore(getMapFile(), tileRendererLayer.getRenderThemeFuture(),
				tileRendererLayer.getTextScale(), tileRendererLayer.getDisplayModel(), AndroidGraphicFactory.INSTANCE);
		LabelLayer labelLayer = createLabelLayer(labelStore);
		mapView.getLayerManager().getLayers().add(labelLayer);
		// add a grid layer and a layer showing tile coordinates
	mapView.getLayerManager().getLayers()
				.add(new TileGridLayer(AndroidGraphicFactory.INSTANCE, this.mapView.getModel().displayModel));
		mapView.getLayerManager().getLayers()
				.add(new TileCoordinatesLayer(AndroidGraphicFactory.INSTANCE, this.mapView.getModel().displayModel));
	}

	protected LabelLayer createLabelLayer(LabelStore labelStore) {
		return new ThreadedLabelLayer(AndroidGraphicFactory.INSTANCE, labelStore);
	}

	@Override
	protected int getLayoutId() {
		return R.layout.rotation;
	}

	@Override
	protected float getScreenRatio() {
		// just to get the cache bigger right now.
		return 2f;
	}

}
