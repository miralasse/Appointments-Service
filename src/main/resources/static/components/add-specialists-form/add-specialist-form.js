import React from 'react';

class AddSpecialistForm extends React.Component {

    constructor(props) {

        super(props);

        this.initialState = {
            id: null,
            name: '',
            active: true,
            organizationId: props.organizationId
        };
        this.state = this.initialState;
    }

    handleChangeName = (event) => {
        this.setState({
            name: event.target.value
        });
    };


    submitForm = () => {

        const specialist = this.state;
        this.addSpecialist(specialist);
        this.setState(this.initialState);
    };

    addSpecialist = (specialist) => {

        const specialistsUrl = '/specialists/';
        fetch(
            specialistsUrl,
            {
                credentials: 'include',
                mode: 'cors',
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify(specialist)
            }
        )
            .then(() => {
                this.getAllSpecialists();
            })
            .catch((error) => {
                alert(`При добавлении возникла ошибка: ${error}`);
            });
    };

    getAllSpecialists = () => {
        this.props.getAllSpecialists();
    };

    render() {

        const { name } = this.state;

        return (
            <div>
                <form className="form-inline justify-content-between">
                    <label htmlFor="inputSpecialistName" className="sr-only">Наименование</label>
                    <input type="text"
                           value={name}
                           onChange={this.handleChangeName}
                           placeholder={"Добавить нового специалиста"}
                           className="form-control"
                           id="inputSpecialistName"
                    />

                    <input type="button"
                           value="Добавить"
                           onClick={this.submitForm}
                           className="btn btn-success"
                           disabled={!name}/>
                </form>
                <div className="mb-5"/>
            </div>

        );
    }
}

export default AddSpecialistForm;