import React from 'react';

import WarningPopup from "../warning-popup";


class ServicesTable extends React.Component {

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


    getAllServices = () => {
        this.props.getAllServices();
    };

    removeService = () => {

        const id = this.state.idToRemove;
        const servicesUrl = '/services/';

        fetch(
            servicesUrl + id,
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
                    () => this.getAllServices()
                );
            })
            .catch((error) => {
                alert(`При удалении возникла ошибка: ${error}`);
            });
    };

    makeServiceActive = (id, active) => {

        const servicesUrl = '/services/';

        fetch(
            servicesUrl + id,
            {
                credentials: 'include',
                mode: 'cors',
                method: 'PATCH',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({active: active})
            }
        )
            .then(() => this.getAllServices())
            .catch((error) => {
                alert(`При изменении статуса возникла ошибка: ${error}`);
            });
    };


    render() {

        const cancelRemove = this.cancelRemove;
        const removeService = this.removeService;

        const showRemovingPopup = this.state.isRemovingPopupVisible
            ? <WarningPopup>
                <div>
                    <div className="m-2">Вы действительно хотите удалить цель обращения?</div>
                    <ButtonCancel cancelRemove={cancelRemove}/>
                    <ButtonRemove removeService={removeService}/>
                </div>
            </WarningPopup>
            : '';

        const showWarningPopup = this.state.isWarningPopupVisible
            ? <WarningPopup>
                <div>
                    <div className="alert alert-danger m-2" role="alert">
                        Эту услугу нельзя удалить, поскольку существуют расписания, ссылающиеся на нее.
                        Деактивируйте услугу вместо удаления.
                    </div>
                    <button className="btn btn-primary" onClick={this.closeWarning}>
                        Понятно
                    </button>
                </div>
            </WarningPopup>
            : '';

        const services = this.props.services;
        const setIdToRemove = this.setIdToRemove;
        const makeServiceActive = this.makeServiceActive;


        return (
            <div>
                <table className="table table-sm table-striped customTable">
                    <colgroup style={{width: 55 + '%'}}/>
                    <colgroup style={{width: 20 + '%'}}/>
                    <colgroup style={{width: 25 + '%'}}/>
                    <TableHeader/>
                    <TableBody services={services} setIdToRemove={setIdToRemove} makeServiceActive={makeServiceActive}/>
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

    const rows = props.services.map((service) => {

        const activateButton = service.active
            ? <button className="btn btn-warning"
                      onClick={() => props.makeServiceActive(service.id, false)}>
                Деактивировать
            </button>
            : <button className="btn btn-info"
                      onClick={() => props.makeServiceActive(service.id,true)}>
                Активировать
            </button>;

        const activeText = service.active ? 'Активна' : 'Не активна';

        return (
            <tr key={service.id}>
                <td>{service.name}</td>
                <td>{activeText}</td>
                <td className="button-container">
                    {activateButton}
                    <button className="btn btn-danger" onClick={() => props.setIdToRemove(service.id)}>Удалить
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
                onClick={props.removeService}>
            Да, удалить
        </button>
    );
};

const ButtonCancel = (props) => {
    return (
        <button className="btn btn-secondary m-4"
                onClick={props.cancelRemove}>
            Отменить
        </button>
    );
};

export default ServicesTable;