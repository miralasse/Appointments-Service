import React from 'react';

import WarningPopup from "../warning-popup";


class SpecialistsTable extends React.Component {

    constructor(props) {

        super(props);
        this.state = {
            isRemovingPopupVisible: false,
            idToRemove: null,
            isWarningPopupVisible: false
        };
    }

    cancelRemove = () => {
        this.setState({
            idToRemove: null,
            isRemovingPopupVisible: false
        });
    };

    setIdToRemove = (id) => {
        this.setState({
            idToRemove: id,
            isRemovingPopupVisible: true
        });
    };

    showWarning = () => {
        this.setState({
            isWarningPopupVisible: true
        });
    };

    closeWarning = () => {
        this.setState({
            isWarningPopupVisible: false
        });
    };

    getAllSpecialists = () => {
        this.props.getAllSpecialists();
    };

    removeSpecialist = () => {

        const id = this.state.idToRemove;
        const specialistsUrl = '/specialists/';

        fetch(
            specialistsUrl + id,
            {
                credentials: 'include',
                mode: 'cors',
                method: 'DELETE',
                headers: {'Content-Type': 'application/json'}
            }
        )
            .then((response) => {
                if (response.status === 409) {
                    this.showWarning();
                }
            })
            .then(() => {
                this.setState(
                    {
                        idToRemove: null,
                        isRemovingPopupVisible: false
                    },
                    () => this.getAllSpecialists()
                );
            })
            .catch((error) => {
                alert(`При удалении возникла ошибка: ${error}`);
            });
    };

    makeSpecialistActive = (id, active) => {

        const specialistsUrl = '/specialists/';

        fetch(
            specialistsUrl + id,
            {
                credentials: 'include',
                mode: 'cors',
                method: 'PATCH',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({active: active})
            }
        )
            .then(() => this.getAllSpecialists())
            .catch((error) => {
                alert(`При изменении статуса возникла ошибка: ${error}`);
            });
    };


    render() {

        const cancelRemove = this.cancelRemove;
        const removeSpecialist = this.removeSpecialist;

        const showRemovingPopup = this.state.isRemovingPopupVisible
            ? <WarningPopup>
                <div>
                    <div className="m-2">Вы действительно хотите удалить специалиста?</div>
                    <ButtonCancel cancelRemove={cancelRemove}/>
                    <ButtonRemove removeSpecialist={removeSpecialist}/>
                </div>
            </WarningPopup>
            : '';

        const showWarningPopup = this.state.isWarningPopupVisible
            ? <WarningPopup>
                <div>
                    <div className="alert alert-danger m-2" role="alert">
                        Этого специалиста нельзя удалить, поскольку существуют расписания, ссылающиеся на него.
                        Деактивируйте специалиста вместо удаления.
                    </div>
                    <button className="btn btn-primary" onClick={this.closeWarning}>
                        Понятно
                    </button>
                </div>
            </WarningPopup>
            : '';

        const specialists = this.props.specialists;
        const setIdToRemove = this.setIdToRemove;
        const makeSpecialistActive = this.makeSpecialistActive;

        return (
            <div>
                <table className="table table-sm table-striped customTable">
                    <colgroup style={{width: 55 + '%'}}/>
                    <colgroup style={{width: 20 + '%'}}/>
                    <colgroup style={{width: 25 + '%'}}/>
                    <TableHeader/>
                    <TableBody specialists={specialists} setIdToRemove={setIdToRemove} makeSpecialistActive={makeSpecialistActive}/>
                </table>
                {showRemovingPopup}
                {showWarningPopup}
            </div>
        );
    }
}

const TableHeader = () => {
    return (
        <thead>
        <tr>
            <th>Наименование</th>
            <th>Активность</th>
            <th>Действия</th>
        </tr>
        </thead>
    );
};

const TableBody = (props) => {

    const rows = props.specialists.map((specialist) => {

        const activateButton = specialist.active
            ? <button className="btn btn-warning"
                      onClick={() => props.makeSpecialistActive(specialist.id, false)}>
                Деактивировать
            </button>
            : <button className="btn btn-info"
                      onClick={() => props.makeSpecialistActive(specialist.id,true)}>
                Активировать
            </button>;

        const activeText = specialist.active ? 'Активен' : 'Не активен';

        return (
            <tr key={specialist.id}>
                <td>{specialist.name}</td>
                <td>{activeText}</td>
                <td className="button-container">
                    {activateButton}
                    <button className="btn btn-danger" onClick={() => props.setIdToRemove(specialist.id)}>Удалить
                    </button>
                </td>
            </tr>
        )
    });

    return (
        <tbody>{rows}</tbody>
    );
};


const ButtonRemove = (props) => {
    return (
        <button className="btn btn-danger mx-auto"
                onClick={props.removeSpecialist}>
            Да, удалить
        </button>
    );
};

const ButtonCancel = (props) => {
    return (
        <button className="btn btn-secondary mr-4"
                onClick={props.cancelRemove}>
            Отменить
        </button>
    );
};

export default SpecialistsTable;