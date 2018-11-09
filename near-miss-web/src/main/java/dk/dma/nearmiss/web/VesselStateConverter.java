package dk.dma.nearmiss.web;

import dk.dma.nearmiss.rest.generated.model.VesselState;

/**
 * This class converts one VesselState entity to the VesselState model type.
 */
public class VesselStateConverter {
    private final dk.dma.nearmiss.db.entity.VesselState entity;

    public VesselStateConverter(dk.dma.nearmiss.db.entity.VesselState entity) {
        this.entity = entity;
    }

    public VesselState convert() {

        VesselState model = new VesselState();
        model.setMmsi((long) entity.getMmsi());
        model.setLat(entity.getLatitude());

        /*
  @JsonProperty("lon")
  private Double lon = null;

  @JsonProperty("time")
  private OffsetDateTime time = null;

  @JsonProperty("sog")
  private Double sog = null;

  @JsonProperty("cog")
  private Double cog = null;

  @JsonProperty("hdg")
  private Double hdg = null;

  @JsonProperty("near-miss-flag")
  private Boolean nearMissFlag = null;

  @JsonProperty("safety-zone")
  private SafetyZone safetyZone = null;

  @JsonProperty("dimensions")
  private Dimensions dimensions = null;
         */
        return model;
    }
}
