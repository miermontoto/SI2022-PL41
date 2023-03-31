package g41.si2022.util.state;

public enum FacturaState {
    PENDIENTE,
    PAGADA,
    EXCESO;
    // No se contempla el estado CANCELADA porque no se debería poder cancelar la factura de un profesor.
    // De igual manera, no hay ninguna regla que establezca a las facturas como "RETRASADAS" ante el impago.
}
