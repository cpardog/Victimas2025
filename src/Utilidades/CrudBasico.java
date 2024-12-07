package Utilidades;

import Modelo.Beneficiario;
import java.util.ArrayList;

/**
 *
 * @author Admin
 */
public interface CrudBasico {

    int insert(Beneficiario bene);
    int update(Beneficiario bene, String beneanterior);
    int delete(Beneficiario bene);
    int selectcantbene(int numres);
    ArrayList<Beneficiario> selectbene(int num_res);
    ArrayList<Beneficiario> select();
}
