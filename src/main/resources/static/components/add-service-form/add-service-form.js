import React from 'react';

class AddServiceForm extends React.Component {

    constructor(props) {

        super(props);
        this.initialState = {
            id: null,
            name: '',
            active: true
        };
        this.state = this.initialState;
    }

    handleChangeName = (event) => {
        this.setState({
            name: event.target.value
        });
    };


    submitForm = () => {

        const service = this.state;
        this.addService(service);
        this.setState(this.initialState);
    };

    addService = (service) => {

        const servicesUrl = '/services/';
        fetch(
            servicesUrl,
            {
                credentials: 'include',
                mode: 'cors',
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify(service)
            }
        )
            .then(() => {
                this.getAllServices();
            })
            .catch((error) => {
                alert(`При добавлении возникла ошибка: ${error}`);
            });
    };

    getAllServices = () => {
        this.props.getAllServices();
    };

    render() {

        const { name } = this.state;

        return (
            <div>
                <form className="form-inline justify-content-between">
                    <label htmlFor="inputServiceName" className="sr-only">Наименование</label>
                    <input type="text"
                           value={name}
                           onChange={this.handleChangeName}
                           placeholder={"Добавить новую цель обращения"}
                           className="form-control"
                           id="inputServiceName"
                    />

                    <input type="button"
                           value="Добавить"
                           onClick={this.submitForm}
                           className="btn btn-success"
                           disabled={!name}/>
                </form>
            </div>

        );
    }
}

export default AddServiceForm;